package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.reservation.MyReservationResponse;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPAtest {
    @Nested
    @DataJpaTest
    class JpaTest {
        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private TimeRepository timeRepository;

        @Test
        void 사단계() {
            Time time = new Time("10:00");
            entityManager.persist(time);
            entityManager.flush();

            Time persistTime = timeRepository.findById(time.getId()).orElse(null);

            assertThat(persistTime.getValue()).isEqualTo(time.getValue());
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
}