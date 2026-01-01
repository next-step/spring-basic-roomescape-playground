package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
public class MissionStepTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Test
    void 사단계() {
        Time time = new Time("10:00");
        entityManager.persist(time);

        entityManager.flush();
        entityManager.clear();

        Time persistTime = entityManager.find(Time.class, time.getId());

        assertThat(persistTime).isNotNull(); // null 체크 추가
        assertThat(persistTime.getValue()).isEqualTo(time.getValue());
    }
}