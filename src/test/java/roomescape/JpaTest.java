package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.bytebuddy.asm.Advice.Local;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.reservationTime.ReservationTime;
import roomescape.reservationTime.ReservationTimeRepository;

@DataJpaTest
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationTimeRepository timeRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 사단계() {
        ReservationTime time = new ReservationTime("10:00");
        entityManager.persist(time);
        entityManager.flush();

        ReservationTime persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getTimeValue()).isEqualTo(time.getTimeValue());
    }

    @Test
    @DisplayName("구현 코드 결과를 확인하는 목적이 아닌, 설정에 따른 쿼리를 확인해보기위한 테스트입니다.")
    void ManyToOne_쿼리_확인() {
        List<Reservation> times =  reservationRepository.findAll();

        System.out.println("=== FindAll Join Query ===");

        List<LocalTime> localTimes = reservationRepository.findAll().stream()
                .map(reservation -> reservation.getTime().getTimeValue())
                .toList();

        System.out.println("=== Fetch Join Query ===");

        List<LocalTime> fetchedLocalTimes = reservationRepository.findAllWithReservationTime().stream()
                .map(reservation -> reservation.getTime().getTimeValue())
                .toList();
    }
}
