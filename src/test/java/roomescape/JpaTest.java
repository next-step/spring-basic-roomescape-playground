package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import roomescape.auth.config.utils.TokenProvider;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

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