package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.Member;
import roomescape.reservation.MyReservationResponse;
import roomescape.reservation.ReservationResponse;
import roomescape.waiting.WaitingResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Test
    void 일단계() {
        String token = createToken("admin@email.com", "password");
        assertThat(token).isNotBlank();
    }

    @Test
    void 이단계() {
        String token = createToken("admin@email.com", "password");  // 일단계에서 토큰을 추출하는 로직을 메서드로 따로 만들어서 활용하세요.

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-02");
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
        params.put("date", "2024-03-03");

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

        String token = response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

        return token;
    }

    @Test
    void 삼단계() {
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

    private final long milliseconds = 1000;
    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private String createExpiredToken(Long memberId) {
        long now = new Date().getTime();
        Date expiredDate = new Date(now - milliseconds);

        return Jwts.builder()
                .setSubject(memberId.toString())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .setExpiration(expiredDate)
                .compact();
    }

    @Test
    @DisplayName("만료된 토큰을 요청하면 예외이다.")
    void expiredToken() {
        String expiredToken = createExpiredToken(1L);

        RestAssured.given().log().all()
                .cookie("token", expiredToken)
                .get("/admin")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("토큰이 없는 경우 401을 반환한다.")
    void noToken() {
        RestAssured.given().log().all()
                .get("/admin")
                .then().log().all()
                .statusCode(401);
    }


    @Test
    @DisplayName("잘못된 토큰일 경우 401을 반환한다.")
    void wrongToken() {
        RestAssured.given().log().all()
                .cookie("token", "invalidToken")
                .get("/admin")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("로그인하지 않은 경우 401을 반환한다.")
    void noLogin() {
        RestAssured.given().log().all()
                .get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    void 오단계() {
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
    void 육단계() {
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
                .filter(it -> it.getId() == waiting.getId())
                .filter(it -> !it.getStatus().equals("예약"))
                .findFirst()
                .map(it -> it.getStatus())
                .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }

    @Test
    @DisplayName("예약 대기 취소")
    void delete_waiting() {
        String brownToken = createToken("brown@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        // 1. 예약 대기 생성
        WaitingResponse waiting = RestAssured.given()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        // 2. 생성한 예약 대기 취소
        RestAssured.given()
                .cookie("token", brownToken)
                .delete("/waitings/" + waiting.getId())
                .then()
                .statusCode(204);

        // 3. 내 예약 목록에서 삭제되었는지 확인
        List<MyReservationResponse> myReservations = RestAssured.given()
                .cookie("token", brownToken)
                .get("/reservations-mine")
                .then()
                .statusCode(200)
                .extract().jsonPath()
                .getList(".", MyReservationResponse.class);

        assertThat(myReservations.stream()
                .noneMatch(it -> it.getStatus().contains("예약대기")))
                .isTrue();
    }

    @Test
    @DisplayName("중복 예약 방지")
    void noTwoReservation() {
        String brownToken = createToken("brown@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2026-09-29");
        params.put("time", "1");
        params.put("theme", "1");

        // 예약하기
        RestAssured.given()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then()
                .statusCode(201);

        // 같은 날짜,시간, 테마로 다시 예약하면 실패
        RestAssured.given()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("중복 예약 대기 방지")
    void noTwoWaiting() {
        String brownToken = createToken("brown@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2026-09-29");
        params.put("time", "1");
        params.put("theme", "1");

        // 예약 대기 생성
        RestAssured.given()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then()
                .statusCode(201);

        // 같은 회원이 동일한 조건으로 다시 신청하면 실패
        RestAssured.given()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then()
                .statusCode(409);
    }
}
