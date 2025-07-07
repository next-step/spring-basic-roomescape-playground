package roomescape.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.exception.RoomEscapeException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Test
    void 예약이_정상_생성된다() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(null, LocalDate.parse("2025-07-05"), 1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "석준", "email@email.com");

        //when
        ReservationResponse response = reservationService.save(reservationRequest, loginMember);

        //then
        assertThat(response.name()).isEqualTo("석준");
        assertThat(response.date()).isEqualTo(reservationRequest.date());
        assertThat(response.theme()).isEqualTo("테마1");
        assertThat(response.time()).isEqualTo("10:00");
    }

    @Test
    void 존재하지_않는_시간_예약하면_예외를_던진다() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(null, LocalDate.parse("2025-07-05"), 1L, 999L);
        LoginMember loginMember = new LoginMember(1L, "어드민", "admin@email.com");

        //then
        assertThatThrownBy(() -> reservationService.save(reservationRequest, loginMember))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("해당 시간 정보를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_테마_예약하면_예외를_던진다() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(null, LocalDate.parse("2025-07-05"), 999L, 1L);
        LoginMember loginMember = new LoginMember(1L, "어드민", "admin@email.com");

        //then
        assertThatThrownBy(() -> reservationService.save(reservationRequest, loginMember))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("해당 테마 정보를 찾을 수 없습니다.");
    }

    @Test
    void 예약하려는_사용자를_찾을_수_없으면_예외를_던진다() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(null, LocalDate.parse("2025-07-05"), 1L, 1L);
        LoginMember loginMember = new LoginMember(999L, "어드민", "admin@email.com");

        //then
        assertThatThrownBy(() -> reservationService.save(reservationRequest, loginMember))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 중복_예약을_하면_예외를_던진다() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(null, LocalDate.parse("2025-07-05"), 1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "어드민", "admin@email.com");

        //when
        reservationService.save(reservationRequest, loginMember);

        //then
        assertThatThrownBy(() -> reservationService.save(reservationRequest, loginMember))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 예약을 했습니다.");
    }

}
