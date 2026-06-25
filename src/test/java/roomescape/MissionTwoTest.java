package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.ReservationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionTwoTest {

    @LocalServerPort
    private int port;

    @Test
    void 이름이_없으면_로그인_회원_이름으로_예약한다() {
        String token = createToken("admin@email.com", "password");
        Map<String, String> params = createReservationParams();

        ExtractableResponse<Response> response = createReservation(token, params);

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("어드민");
    }

    @Test
    void 이름이_있으면_요청_이름으로_예약한다() {
        String token = createToken("admin@email.com", "password");
        Map<String, String> params = createReservationParams();
        params.put("name", "브라운");

        ExtractableResponse<Response> response = createReservation(token, params);

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).getName()).isEqualTo("브라운");
    }

    private Map<String, String> createReservationParams() {
        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");
        return params;
    }

    private ExtractableResponse<Response> createReservation(String token, Map<String, String> params) {
        return RestAssured.given().log().all()
                .port(port)
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }

    private String createToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .port(port)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract();

        return response.cookie("token");
    }
}
