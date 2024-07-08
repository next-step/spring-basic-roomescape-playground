package roomescape;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@DataJpaTest
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Test
    void 사단계() {
        final Time time = new Time("10:00");

        System.out.println("before Persist");
        entityManager.persist(time);
        System.out.println("after Persist");
        entityManager.flush();

        final Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }
}
