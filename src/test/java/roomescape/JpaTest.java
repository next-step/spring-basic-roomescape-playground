package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.reservationTime.ReservationTime;
import roomescape.reservationTime.ReservationTimeRepository;

@DataJpaTest
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Test
    void 사단계() {
        ReservationTime time = new ReservationTime("10:00");
        entityManager.persist(time);
        entityManager.flush();

        ReservationTime persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getTimeValue()).isEqualTo(time.getTimeValue());
    }
}
