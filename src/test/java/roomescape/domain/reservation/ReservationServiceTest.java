package roomescape.domain.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.global.exception.ConflictException;
import roomescape.global.exception.NotFoundException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ReservationService.class)
public class ReservationServiceTest {

    private final Long time = 1L;

    @Autowired
    private ReservationService reservationService;

    @Test
    void createReservation을_존재하지_않는_themeId로_호출하면_예외가_발생한다() {
        // then
        Assertions.assertThrows(
                NotFoundException.class,

                // when
                () -> reservationService.createReservation(1L, "더미_유저", LocalDate.of(9999, 12, 30), -1L, time)
        );
    }

    @Test
    void createReservation을_존재하지_않는_time으로_호출하면_예외가_발생한다() {
        // then
        Assertions.assertThrows(
                NotFoundException.class,

                // when
                () -> reservationService.createReservation(1L, "더미_유저", LocalDate.of(9999, 12, 30), 1L, -1L)
        );
    }

    @Test
    void 이미_예약한_날짜_시간_테마에_예약을_생성하면_예외를_던진다() {
        // then
        Assertions.assertThrows(
                ConflictException.class,
                () -> reservationService.createReservation(1L, "더미_어드민", LocalDate.of(9999, 12, 31), 1L, time)
        );
    }

    @Test
    void 예약에_성공한다() {
        // given
        LocalDate reserveDate = LocalDate.of(9999, 12, 30);
        // when
        Reservation reservation = reservationService.createReservation(1L, "더미_유저", reserveDate, 1L, time);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getId()).isNotNull();
        assertThat(reservation.getDate()).isEqualTo(reserveDate);
    }
}
