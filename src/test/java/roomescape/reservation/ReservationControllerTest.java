package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.member.JwtProvider;
import roomescape.member.dto.MemberResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

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
        String token = jwtProvider.generateToken(new MemberResponse(1L, "어드민", "admin@email.com", "ADMIN"));
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
