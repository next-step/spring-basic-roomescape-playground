package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ReservationService.class)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 추가_쿼리_없이_모든_예약_목록_응답_반환_성공() {
        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(3);
    }

    @Test
    void 추가_쿼리_없이_내_예약_목록_응답_반환_성공() {
        // when
        List<MyReservationResponse> reservations = reservationService.readAllByMember("admin@email.com");

        // then
        assertThat(reservations.size()).isEqualTo(3);
    }
}
