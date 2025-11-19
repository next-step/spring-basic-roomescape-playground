package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.time.ParticipationTime;
import roomescape.time.ParticipationTimeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParticipationTimeRepository participationTimeRepository;

    @Test()
    void testTimeSave() {
        ParticipationTime time = new ParticipationTime("10:00");
        entityManager.persist(time);
        entityManager.flush();

        Optional<ParticipationTime> savedTime = participationTimeRepository.findById(time.getId());
        assertThat(savedTime.isPresent()).isTrue();
        assertThat(savedTime.get().getTime()).isEqualTo(time.getTime());
    }
}
