package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import roomescape.time.entity.Time;
import roomescape.time.repository.JpaTimeRepository;
import roomescape.time.repository.TimeRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaTimeRepository.class)
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

        assertThat(persistTime.getTimeValue()).isEqualTo(time.getTimeValue());
    }
}
