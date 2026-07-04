package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import roomescape.reservation.MyReservationResponse;
import roomescape.reservation.ReservationResponse;
import roomescape.waiting.WaitingResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Test
    void 일단계() {
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
    }
    @Test
    void 이단계() {
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
    void 삼단계() {
        String brownToken = createToken("brown@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/admin")
                .then().log().all()
                .statusCode(403);

        String adminToken = createToken("admin@email.com", "password");

        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/admin")
                .then().log().all()
                .statusCode(200);
    }
    @Value("${jwt.secret}")
    private String secretKey;

    private String createExpiredToken() {
        Date past = new Date(System.currentTimeMillis() - 60000);
        return io.jsonwebtoken.Jwts.builder()
                .setSubject("1")
                .claim("name", "브라운")
                .claim("role", "USER")
                .setIssuedAt(past)
                .setExpiration(past)
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
    @Test
    void 토큰_만료_테스트() {
        String expiredToken = createExpiredToken();
        RestAssured.given().log().all()
                .cookie("token", expiredToken)
                .get("/admin")
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
    void 육단계_대기_순번_응답() {
        String brownToken = createToken("brown@email.com", "password");
        String adminToken = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-02");
        params.put("time", "1");
        params.put("theme", "1");

        // 같은 슬롯에 첫 번째 대기 → waitingNumber = 1
        WaitingResponse first = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        assertThat(first.getWaitingNumber()).isEqualTo(1L);

        // 다른 회원이 같은 슬롯에 대기 → waitingNumber = 2
        WaitingResponse second = RestAssured.given().log().all()
                .body(params)
                .cookie("token", adminToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        assertThat(second.getWaitingNumber()).isEqualTo(2L);
    }

    @Test
    void 육단계_중복_대기_불가() {
        String brownToken = createToken("brown@email.com", "password");
        String adminToken = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        // 첫 대기는 성공
        RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201);

        // 같은 회원이 같은 슬롯에 다시 대기 → 400
        RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(400);

        // 이미 예약한 슬롯(어드민이 2024-03-01, 1, 1 예약 보유)에 대기 → 400
        RestAssured.given().log().all()
                .body(params)
                .cookie("token", adminToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 육단계_대기_취소() {
        String brownToken = createToken("brown@email.com", "password");
        String adminToken = createToken("admin@email.com", "password");

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-02");
        params.put("time", "1");
        params.put("theme", "1");

        WaitingResponse brownWaiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        WaitingResponse adminWaiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", adminToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        // 남의 대기는 취소 불가 → 400
        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .delete("/waitings/" + adminWaiting.getId())
                .then().log().all()
                .statusCode(400);

        // 본인 대기 취소 → 204
        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .delete("/waitings/" + brownWaiting.getId())
                .then().log().all()
                .statusCode(204);

        // 취소한 대기는 내 예약 목록에서 사라짐
        List<MyReservationResponse> myReservations = RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        assertThat(myReservations)
                .noneMatch(it -> it.getId().equals(brownWaiting.getId()) && !it.getStatus().equals("예약"));
    }
}
