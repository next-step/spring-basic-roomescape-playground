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
public class MissionSixTest {

    @LocalServerPort
    private int port;

    @Test
    void 대기_등록_시_대기_순번을_반환한다() {
        String brownToken = createToken("brown@email.com", "password");

        int waitingNumber = createWaiting(brownToken, createReservationParams())
                .jsonPath().getInt("waitingNumber");

        assertThat(waitingNumber).isEqualTo(1);
    }

    @Test
    void 내_예약_목록에서_대기_상태는_순번으로_표시된다() {
        String brownToken = createToken("brown@email.com", "password");
        createWaiting(brownToken, createReservationParams());

        List<ReservationMineResponse> reservations = getMyReservations(brownToken);

        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getStatus()).isEqualTo("1번째");
    }

    @Test
    void 같은_날짜와_테마의_두번째_대기는_2번째를_반환한다() {
        String adminToken = createToken("admin@email.com", "password");
        String brownToken = createToken("brown@email.com", "password");

        createWaiting(adminToken, createReservationParams());
        int waitingNumber = createWaiting(brownToken, createReservationParams())
                .jsonPath().getInt("waitingNumber");

        assertThat(waitingNumber).isEqualTo(2);
    }

    @Test
    void 대기_순번은_같은_날짜와_테마안에서_시간순으로_계산한다() {
        String adminToken = createToken("admin@email.com", "password");
        String brownToken = createToken("brown@email.com", "password");

        createWaiting(adminToken, createReservationParams("2024-03-02", "1", "2"));
        ExtractableResponse<Response> response = createWaiting(
                brownToken,
                createReservationParams("2024-03-02", "1", "3")
        );

        assertThat(response.jsonPath().getInt("waitingNumber")).isEqualTo(2);
    }

    @Test
    void 다른_날짜의_대기는_순번_계산에_영향주지_않는다() {
        String adminToken = createToken("admin@email.com", "password");
        String brownToken = createToken("brown@email.com", "password");

        createWaiting(adminToken, createReservationParams("2024-03-02", "1", "1"));
        int waitingNumber = createWaiting(brownToken, createReservationParams("2024-03-01", "1", "1"))
                .jsonPath().getInt("waitingNumber");

        assertThat(waitingNumber).isEqualTo(1);
    }

    @Test
    void 다른_테마의_대기는_순번_계산에_영향주지_않는다() {
        String adminToken = createToken("admin@email.com", "password");
        String brownToken = createToken("brown@email.com", "password");

        createWaiting(adminToken, createReservationParams("2024-03-01", "2", "1"));
        int waitingNumber = createWaiting(brownToken, createReservationParams("2024-03-01", "1", "1"))
                .jsonPath().getInt("waitingNumber");

        assertThat(waitingNumber).isEqualTo(1);
    }

    @Test
    void 본인_대기를_취소할_수_있다() {
        String brownToken = createToken("brown@email.com", "password");

        Long waitingId = createWaiting(brownToken, createReservationParams())
                .jsonPath().getLong("id");

        RestAssured.given().log().all()
                .port(port)
                .cookie("token", brownToken)
                .when().delete("/waitings/" + waitingId)
                .then().log().all()
                .statusCode(204);

        List<ReservationMineResponse> reservations = getMyReservations(brownToken);
        assertThat(reservations).isEmpty();
    }

    @Test
    void 다른_회원의_대기는_취소할_수_없다() {
        String adminToken = createToken("admin@email.com", "password");
        String brownToken = createToken("brown@email.com", "password");

        Long waitingId = createWaiting(adminToken, createReservationParams())
                .jsonPath().getLong("id");

        RestAssured.given().log().all()
                .port(port)
                .cookie("token", brownToken)
                .when().delete("/waitings/" + waitingId)
                .then().log().all()
                .statusCode(401);
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
        return createReservationParams("2024-03-01", "1", "1");
    }

    private Map<String, String> createReservationParams(String date, String theme, String time) {
        Map<String, String> params = new HashMap<>();
        params.put("date", date);
        params.put("theme", theme);
        params.put("time", time);
        return params;
    }

    private ExtractableResponse<Response> createWaiting(String token, Map<String, String> params) {
        return RestAssured.given().log().all()
                .port(port)
                .body(params)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post("/waitings")
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
