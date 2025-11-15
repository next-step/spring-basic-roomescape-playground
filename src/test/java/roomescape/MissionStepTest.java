package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.MyReservationResponse;
import roomescape.reservation.ReservationResponse;
import roomescape.waiting.WaitingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Test
    @DisplayName("로그인시 토큰 정상 발급 테스트")
    void login_shouldReturnAccessToken() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "admin@email.com");
        params.put("password", "password");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
        assertThat(token).isNotBlank();

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
    }

    @Test
    @DisplayName("예약 생성시 name이 비어있으면 로그인한 회원 이름을 사용하는지 테스트")
    void createReservation_shouldUseLoginMemberName_whenNameIsNotProvided() {
        String token = createToken("admin@email.com", "password");  // 일단계에서 토큰을 추출하는 로직을 메서드로 따로 만들어서 활용하세요.

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");

        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("Admin 권한이 있는 사용자만 '/admin' 엔드포인트에 접근할 수 있는지 테스트")
    void cannotAccessAdminEndpoint_whenNonAdminUser() {
        String brownToken = createToken("brown@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);

        String adminToken = createToken("admin@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    private String createToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        return response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

    }

    @Test
    void GetMyReservationListTest() {
        String adminToken = createToken("admin@email.com", "password");

        List<MyReservationResponse> reservations = RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        assertThat(reservations).hasSize(3);
    }

    @Test
    void testWaitingSystem() {
        String brownToken = createToken("brown@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        // 예약 대기 생성
        WaitingResponse waiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        // 내 예약 목록 조회
        List<MyReservationResponse> myReservations = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        // 예약 대기 상태 확인
        String status = myReservations.stream()
                .filter(it -> it.getId() == waiting.waitingId())
                .filter(it -> !it.getStatus().equals("예약"))
                .findFirst()
                .map(it -> it.getStatus())
                .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }

    @Test
    @DisplayName("Waiting 생성 시 트랜잭션 커밋 시점에 INSERT 쿼리가 발생하는지 확인")
    void createWaiting_shouldInsertQuery_atTransactionCommit() {
        // SQL 로그 설정을 반드시 활성화 해야 함 (application-test.properties 등)
        // spring.jpa.properties.hibernate.format_sql=true
        // logging.level.org.hibernate.SQL=DEBUG
        // logging.level.org.hibernate.type.descriptor.sql=TRACE

        // 1. 사전 준비: 토큰 발급 및 요청 파라미터 준비
        String token = createToken("brown@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1"); // ID 1번 시간은 09:00라고 가정
        params.put("theme", "1"); // ID 1번 테마라고 가정

        System.out.println("\n--- [관찰 시작] /waitings 호출 시작 ---");

        // 2. /waitings End-point 호출 (WaitingController.create -> WaitingService.create 실행)
        RestAssured.given().log().all()
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201);

        System.out.println("--- [관찰 끝] /waitings 응답 완료 ---\n");

        // 3. 콘솔 로그 분석:
        //    - HTTP 요청 시작 후, 먼저 3개의 SELECT (Member, Time, Theme 조회) 쿼리가 실행됩니다.
        //    - HTTP 응답 코드(201)가 콘솔에 출력되기 직전에 INSERT 쿼리가 출력되는지 확인합니다.
        //    - 이는 @Transactional이 적용된 WaitingService.create() 메서드의 트랜잭션이
        //      컨트롤러에서 반환될 때 커밋되면서, 영속성 컨텍스트에 쌓여있던 Waiting 엔티티의
        //      변경(INSERT) 내용이 데이터베이스에 Flush 되었음을 의미합니다.
    }
}



