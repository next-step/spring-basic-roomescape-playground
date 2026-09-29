package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Test
    void 사단계() {
        Time time = new Time(LocalTime.of(10, 0));
        entityManager.persist(time);
        entityManager.flush();

        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getTimeValue()).isEqualTo(time.getTimeValue());
    }
}
