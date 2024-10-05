package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.auth.JwtProvider;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationResponse;

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

        ExtractableResponse<Response> checkResponse = RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .when().get("/login/check")
            .then().log().all()
            .statusCode(200)
            .extract();

        assertThat(checkResponse.body().jsonPath().getString("name")).isEqualTo("어드민");
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
        assertThat(response.as(ReservationResponse.class).name()).isEqualTo("어드민");

        params.put("name", "브라운");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
            .body(params)
            .cookie("token", token)
            .contentType(ContentType.JSON)
            .post("/reservations")
            .then().log().all()
            .extract();

        assertThat(adminResponse.statusCode()).isEqualTo(201);
        assertThat(adminResponse.as(ReservationResponse.class).name()).isEqualTo("브라운");
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
        roomescape.waiting.dto.WaitingResponse waiting = RestAssured.given().log().all()
            .body(params)
            .cookie("token", brownToken)
            .contentType(ContentType.JSON)
            .post("/waitings")
            .then().log().all()
            .statusCode(201)
            .extract().as(roomescape.waiting.dto.WaitingResponse.class);

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
            .filter(it -> it.id() == waiting.id())
            .filter(it -> !it.status().equals("예약"))
            .findFirst()
            .map(it -> it.status())
            .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }

    @Test
    void 칠단계() {
        Component componentAnnotation = JwtProvider.class.getAnnotation(Component.class);
        assertThat(componentAnnotation).isNull();
    }
}
