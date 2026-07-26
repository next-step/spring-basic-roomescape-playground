package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.MyReservationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class ReservationApiTest extends ApiTest {

    private final static String ACCESS_TOKEN = "access-token";

    @Test
    void 이미_예약된_시간과_테마로_예약하면_409를_반환한다() {
        // given
        String accessToken = 로그인_시도("admin@email.com", "password").cookie(ACCESS_TOKEN);

        Map<String, String> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("time", "1");
        params.put("theme", "1");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(ACCESS_TOKEN, accessToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    void 로그인한_회원은_자신의_예약_목록을_조회할_수_있다() {
        // given
        ExtractableResponse<Response> loginResponse = 로그인_시도("admin@email.com", "password");
        String accessToken = loginResponse.cookie(ACCESS_TOKEN);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(ACCESS_TOKEN, accessToken)
                .when().get("/reservations-mine")
                .then().log().all()
                .extract();
        List<MyReservationResponse> reservations = response.jsonPath().getList(".", MyReservationResponse.class);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(response.statusCode()).isEqualTo(200);
            softAssertions.assertThat(reservations).hasSize(3);
        });
    }

    @Test
    void 인증_정보가_없으면_예약_목록을_조회할_수_없다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations-mine")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(401);
    }
}
