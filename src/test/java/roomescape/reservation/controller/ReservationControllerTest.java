package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Test
    void same_schedule_cannot_be_reserved_twice() {
        String token = createToken("admin@email.com", "password");
        Map<String, String> reservation = Map.of(
                "date", "2024-03-02",
                "time", "1",
                "theme", "1"
        );

        RestAssured.given()
                .body(reservation)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then()
                .statusCode(201);

        ExtractableResponse<Response> duplicateResponse = RestAssured.given()
                .body(reservation)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then()
                .statusCode(400)
                .extract();

        assertThat(duplicateResponse.jsonPath().getString("message"))
                .isEqualTo("이미 예약된 날짜, 테마, 시간입니다.");
    }

    private String createToken(String email, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", password))
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .headers()
                .get("Set-Cookie")
                .getValue()
                .split(";")[0]
                .split("=")[1];
    }
}
