package roomescape;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.auth.config.utils.TokenProvider;
import roomescape.member.Role;
import roomescape.reservation.DTO.ReservationResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "JWT_SECRET=my-super-secret-key-that-is-at-least-32-characters-long")
public class TestStep5 {
    @Autowired
    private TokenProvider tokenProvider;

    @Test
    void testStep5() {
        String adminToken = tokenProvider.createToken("admin@email.com", "password", Role.ADMIN);

        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .cookie("token", adminToken)
                .get("/reservations-mine")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationResponse.class);

        assertThat(reservations).hasSize(3);
    }
}
