package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

@DataJpaTest
@ActiveProfiles("test")
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Test
    void 사단계() {
        Time time = new Time("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getTime()).isEqualTo(time.getTime());
    }

    @Test
    void 팔단계() {
        assertThat(secretKey).isNotBlank();
    }
}
