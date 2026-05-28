package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import roomescape.time.Time;
import roomescape.time.TimeRepository;

@DataJpaTest
public class JpaTest {
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

        assertThat(persistTime.getTime()).isEqualTo(time.getTime());
    }

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public class MissionStepTest {
        @Test
        void 오단계() {
            String adminToken = createToken("admin@email.com", "password");

            List<MyReservationResponse> reservations = RestAssured.given().log().all()
                    .cookie("token", adminToken)
                    .get("/reservations-mine")
                    .then().log().all()
                    .statusCode(200)
                    .extract().jsonPath().getList(".", MyReservationResponse.class);

            assertThat(reservations).hasSize(3);
        }
    }
}
