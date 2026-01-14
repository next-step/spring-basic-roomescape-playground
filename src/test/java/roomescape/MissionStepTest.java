package roomescape;

import roomescape.auth.JwtUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.reservation.MyReservationResponse;
import roomescape.reservation.ReservationResponse;
import roomescape.waiting.WaitingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class MissionStepTest {

    @Test
    void 일단계() {

        Map<String, String> loginParams = new HashMap<>();
        loginParams.put("email", "admin@email.com");
        loginParams.put("password", "password");

        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                                                                 .contentType(ContentType.JSON)
                                                                 .body(loginParams)
                                                                 .when().post("/login")
                                                                 .then().log().all()
                                                                 .statusCode(200)
                                                                 .extract();

        String token = loginResponse.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];

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
    void 이단계() {

        String adminToken = createToken("admin@email.com", "password");

        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("date", "2024-03-01");
        reservationParams.put("time", "4");
        reservationParams.put("theme", "1");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(reservationParams)
                                                            .cookie("token", adminToken)
                                                            .contentType(ContentType.JSON)
                                                            .post("/reservations")
                                                            .then().log().all()
                                                            .extract();

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(201);
            softly.assertThat(response.as(ReservationResponse.class).name()).isEqualTo("어드민");
        });

        reservationParams.put("name", "브라운");
        reservationParams.put("time", "5");

        ExtractableResponse<Response> adminResponse = RestAssured.given().log().all()
                                                                 .body(reservationParams)
                                                                 .cookie("token", adminToken)
                                                                 .contentType(ContentType.JSON)
                                                                 .post("/reservations")
                                                                 .then().log().all()
                                                                 .extract();

        assertSoftly(softly -> {
            softly.assertThat(adminResponse.statusCode()).isEqualTo(201);
            softly.assertThat(adminResponse.as(ReservationResponse.class).name()).isEqualTo("브라운");
        });
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

        WaitingResponse waiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        List<MyReservationResponse> myReservations = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        String status = myReservations.stream()
                .filter(it -> it.reservationId() == waiting.id())
                .filter(it -> !it.status().equals("예약"))
                .findFirst()
                .map(it -> it.status())
                .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }

    @Test
    void 칠단계() {
        Component componentAnnotation = JwtUtils.class.getAnnotation(Component.class);
        assertThat(componentAnnotation).isNull();
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
}
