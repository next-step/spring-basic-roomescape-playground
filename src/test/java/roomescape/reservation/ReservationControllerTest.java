package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.JwtProvider;
import roomescape.member.dto.MemberResponse;
import roomescape.member.enums.Role;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("모든 예약 조회")
    void given_request_when_reservations_then_jsonPath() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getList("name")).contains("어드민");
    }

    @DisplayName("예약 생성")
    @Test
    void given_token_body_when_create_reservations_then_success() {
        String token = jwtProvider.generateToken(new MemberResponse(1L, "어드민", "admin@email.com", Role.ADMIN));
        // given
        Map<String, String> body = Map.of(
                "date", "2025-03-01",
                "time", "1",
                "theme", "1"
        );
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(body)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .post("/reservations")
                .then().log().all()
                .extract();
        // then
        assertThat(response.statusCode()).isEqualTo(201);

    }

    @Test
    @DisplayName("예약 ID 삭제")
    void writeHereTestName() {
        // given
        Long reservationId = 4L;
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .extract();
        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }
}
