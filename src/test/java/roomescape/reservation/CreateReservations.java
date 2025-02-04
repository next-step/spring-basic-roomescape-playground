package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CreateReservations {
    
    @Test
    void 로그인_상태에서_예약자명이_존재하지않는_경우_로그인_정보명으로_예약된다() {
        //given
        String token = createToken("admin@email.com", "password");
        Map<String, String> reservationRequest = createReservationRequest("2024-03-01", null, "1", "1");

        //when
        ReservationResponse reservationResponse = sendCreateReservationsRequest(reservationRequest, token).as(
                ReservationResponse.class);

        //then
        assertThat(reservationResponse.getName()).isEqualTo("어드민");
    }

    @Test
    void 로그인_상태에서_예약자명이_존재하는_경우_예약자명으로_예약된다() {
        //given
        String token = createToken("admin@email.com", "password");
        Map<String, String> reservationRequest = createReservationRequest("2024-03-01", "브라운", "1", "1");

        //when
        ReservationResponse reservationResponse = sendCreateReservationsRequest(reservationRequest, token).as(
                ReservationResponse.class);

        //then
        assertThat(reservationResponse.getName()).isEqualTo("브라운");
    }

    @Test
    void 비로그인_상태에서_예약할_경우_예약에_실패한다() {
        //given
        Map<String, String> reservationRequest = createReservationRequest("2024-03-01", "브라운", "1", "1");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(reservationRequest)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void 유효하지_않은_예약_날짜인_경우_예약에_실패한다() {
        //given
        String token = createToken("admin@email.com", "password");
        Map<String, String> reservationRequest = createReservationRequest(null, "브라운", "1", "1");

        //when
        ExtractableResponse<Response> response = sendCreateReservationsRequest(reservationRequest, token);

        //then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void 유효하지_않은_예약_시간인_경우_예약에_실패한다() {
        //given
        String token = createToken("admin@email.com", "password");
        Map<String, String> reservationRequest = createReservationRequest("2024-03-01", "브라운", null, "1");

        //when
        ExtractableResponse<Response> response = sendCreateReservationsRequest(reservationRequest, token);

        //then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void 유효하지_않은_예약_테마인_경우_예약에_실패한다() {
        //given
        String token = createToken("admin@email.com", "password");
        Map<String, String> reservationRequest = createReservationRequest("2024-03-01", "브라운", "1", null);

        //when
        ExtractableResponse<Response> response = sendCreateReservationsRequest(reservationRequest, token);

        //then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    @NotNull
    private Map<String, String> createReservationRequest(String date, String name, String time, String theme) {
        Map<String, String> reservationRequest = new HashMap<>();
        if (date != null) {
            reservationRequest.put("date", date);
        }
        if (name != null) {
            reservationRequest.put("name", name);
        }
        if (time != null) {
            reservationRequest.put("time", time);
        }
        if (theme != null) {
            reservationRequest.put("theme", theme);
        }
        return reservationRequest;
    }

    private ExtractableResponse<Response> sendCreateReservationsRequest(Map<String, String> reservationRequest,
                                                                        String token) {
        return RestAssured.given().log().all()
                .body(reservationRequest)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();
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
