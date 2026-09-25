package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import roomescape.support.DatabaseTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseTest
class ReservationControllerTest {

    @Test
    void 타인의_예약을_삭제하려고_하면_예외가_발생한다() {
        // given
        String userToken = 로그인_토큰_발급("brown@email.com", "password");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void 관리자는_타인의_예약도_삭제할_수_있다() {
        // given
        String adminToken = 로그인_토큰_발급("admin@email.com", "password");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }

    private String 로그인_토큰_발급(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        ExtractableResponse<Response> response = RestAssured.given()
                .contentType("application/json")
                .body(params)
                .when().post("/login")
                .then()
                .statusCode(200)
                .extract();

        return response.headers().get("Set-Cookie").getValue().split(";")[0].split("=")[1];
    }
}
