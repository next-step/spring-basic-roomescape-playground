package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.ReservationMineResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionFiveTest {

    @LocalServerPort
    private int port;

    @Test
    void 로그인한_회원의_내_예약_목록을_조회한다() {
        String adminToken = createToken("admin@email.com", "password");

        List<ReservationMineResponse> reservations = getMyReservations(adminToken);

        assertThat(reservations).hasSize(3);
    }

    @Test
    void 내_예약_응답_필드를_검증한다() {
        String adminToken = createToken("admin@email.com", "password");

        List<ReservationMineResponse> reservations = getMyReservations(adminToken);

        assertThat(reservations.get(0).getReservationId()).isNotNull();
        assertThat(reservations.get(0).getTheme()).isEqualTo("테마1");
        assertThat(reservations.get(0).getDate()).isEqualTo("2024-03-01");
        assertThat(reservations.get(0).getTime()).isEqualTo("10:00");
        assertThat(reservations.get(0).getStatus()).isEqualTo("예약");
    }

    @Test
    void 로그인하지_않으면_내_예약_조회에_실패한다() {
        RestAssured.given().log().all()
                .port(port)
                .when().get("/reservations-mine")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    void 다른_회원의_예약은_포함되지_않는다() {
        String brownToken = createToken("brown@email.com", "password");

        List<ReservationMineResponse> reservations = getMyReservations(brownToken);

        assertThat(reservations).isEmpty();
    }

    @Test
    void 예약_생성_후_내_예약_목록에_포함된다() {
        String brownToken = createToken("brown@email.com", "password");

        createReservation(brownToken, createReservationParams());

        List<ReservationMineResponse> reservations = getMyReservations(brownToken);

        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getTheme()).isEqualTo("테마1");
        assertThat(reservations.get(0).getDate()).isEqualTo("2024-03-01");
        assertThat(reservations.get(0).getTime()).isEqualTo("10:00");
        assertThat(reservations.get(0).getStatus()).isEqualTo("예약");
    }

    private List<ReservationMineResponse> getMyReservations(String token) {
        return RestAssured.given().log().all()
                .port(port)
                .cookie("token", token)
                .when().get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationMineResponse.class);
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
                .statusCode(201)
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
