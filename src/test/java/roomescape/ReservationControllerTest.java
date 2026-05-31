package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationControllerTest extends BaseControllerTest {

    @DisplayName("로그인한 사용자로 예약을 생성한다")
    @Test
    void create_reservation_as_self() {
        // given
        String adminToken = createToken("admin@email.com", "password");
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("timeId", 1);
        params.put("themeId", 1);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", adminToken)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).name()).isEqualTo("어드민");
    }

    @DisplayName("어드민은 다른 사용자 이름으로 예약을 생성할 수 있다")
    @Test
    void admin_creates_reservation_for_other_member() {
        // given
        String adminToken = createToken("admin@email.com", "password");
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("name", "브라운");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .cookie("token", adminToken)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.as(ReservationResponse.class).name()).isEqualTo("브라운");
    }

    @DisplayName("내 예약 목록을 조회하면 본인의 예약만 반환된다")
    @Test
    void get_my_reservations() {
        // given
        String adminToken = createToken("admin@email.com", "password");

        // when
        List<MyReservationResponse> reservations = RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        // then
        assertThat(reservations).hasSize(3);
    }
}
