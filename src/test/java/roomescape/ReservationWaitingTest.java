package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.LoginMember;
import roomescape.member.Role;
import roomescape.reservation.MyReservationResponse;
import roomescape.waiting.WaitingRequest;
import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationWaitingTest {
    @Autowired
    private WaitingService waitingService;

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
                .filter(it -> it.getId() == waiting.getId())
                .filter(it -> !it.getStatus().equals("예약"))
                .findFirst()
                .map(it -> it.getStatus())
                .orElse(null);

        assertThat(status).isEqualTo("1번째 예약대기");
    }
    private String createToken(String email, String password) {
        Map<String, String> params = Map.of(
                "email", email,
                "password", password
        );
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().statusCode(200)
                .extract()
                .cookie("token");
    }

    @Test
    @DisplayName("기존의 예약이 없을때는 대기하지 못한다.")
    void waitingExceptionTest() {
        WaitingRequest waitingRequest = new WaitingRequest("2026-03-01", 1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "jisu", "querty@gmail.com", Role.USER);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            waitingService.createWaiting(waitingRequest, loginMember);
        });
        assertEquals("예약이 존재할때만 대기할 수 있습니다.", exception.getMessage());
    }
}

