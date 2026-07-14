package roomescape;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import roomescape.auth.config.AllowedRole;
import roomescape.auth.config.utils.TokenProvider;
import roomescape.member.Role;
import roomescape.reservation.DTO.ReservationResponse;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaTest {
    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @MockBean
    private TokenProvider tokenProvider;

    @Test
    void testStep4() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }
}