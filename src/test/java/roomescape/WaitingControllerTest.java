package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.WaitingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingControllerTest extends BaseControllerTest {

    @DisplayName("예약 대기를 생성하면 내 예약 목록에 대기 순번과 함께 조회된다")
    @Test
    void create_waiting_and_check_in_my_reservations() {
        // given
        String brownToken = createToken("brown@email.com", "password");
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("timeId", 1);
        params.put("themeId", 1);

        // when
        WaitingResponse waiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        List<MyReservationResponse> myReservations = RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        // then
        String status = myReservations.stream()
                .filter(it -> it.reservationId().equals(waiting.id()))
                .filter(it -> !it.status().equals("예약"))
                .findFirst()
                .map(MyReservationResponse::status)
                .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }

    @DisplayName("예약 대기를 취소하면 내 예약 목록에서 사라진다")
    @Test
    void cancel_waiting() {
        // given
        String brownToken = createToken("brown@email.com", "password");
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("timeId", 1);
        params.put("themeId", 1);

        WaitingResponse waiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        // when
        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .delete("/waitings/cancel/" + waiting.id())
                .then().log().all()
                .statusCode(204);

        // then
        List<MyReservationResponse> myReservations = RestAssured.given().log().all()
                .cookie("token", brownToken)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MyReservationResponse.class);

        boolean exists = myReservations.stream()
                .anyMatch(it -> it.reservationId().equals(waiting.id()));
        assertThat(exists).isFalse();
    }

    @DisplayName("어드민은 다른 사람의 예약 대기를 취소할 수 있다")
    @Test
    void admin_cancels_other_members_waiting() {
        // given
        String brownToken = createToken("brown@email.com", "password");
        String adminToken = createToken("admin@email.com", "password");
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("timeId", 1);
        params.put("themeId", 1);

        WaitingResponse waiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", brownToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        // when & then
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .delete("/waitings/cancel/" + waiting.id())
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("일반 유저는 다른 사람의 예약 대기를 취소하려 하면 400을 반환한다")
    @Test
    void user_cannot_cancel_other_members_waiting() {
        // given
        String adminToken = createToken("admin@email.com", "password");
        String brownToken = createToken("brown@email.com", "password");
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-03-01");
        params.put("timeId", 1);
        params.put("themeId", 1);

        WaitingResponse waiting = RestAssured.given().log().all()
                .body(params)
                .cookie("token", adminToken)
                .contentType(ContentType.JSON)
                .post("/waitings")
                .then().log().all()
                .statusCode(201)
                .extract().as(WaitingResponse.class);

        // when & then
        RestAssured.given().log().all()
                .cookie("token", brownToken)
                .delete("/waitings/cancel/" + waiting.id())
                .then().log().all()
                .statusCode(400);
    }
}
