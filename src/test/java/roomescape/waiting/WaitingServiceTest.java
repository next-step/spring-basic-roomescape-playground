package roomescape.waiting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.exception.RoomEscapeException;
import roomescape.reservation.ReservationRequest;
import roomescape.reservation.ReservationService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class WaitingServiceTest {

    @Autowired
    WaitingService waitingService;
    @Autowired
    ReservationService reservationService;

    @Test
    void 에약대기가_정상적으로_작동한다() {
        //given
        WaitingRequest request = new WaitingRequest(LocalDate.parse("2025-07-05"), 1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "어드민", "admin@email.com");

        //when
        WaitingResponse response = waitingService.save(request, loginMember);

        //then
        assertThat(response.name()).isEqualTo(loginMember.name());
        assertThat(response.date()).isEqualTo(request.date());
        assertThat(response.theme()).isEqualTo("테마1");
        assertThat(response.time()).isEqualTo("10:00");
        assertThat(response.waitingNumber()).isEqualTo(1L);
    }

    @Test
    void 중복_예약대기를하면_예외를_던진다() {
        //given
        WaitingRequest request1 = new WaitingRequest(LocalDate.parse("2025-07-05"), 1L, 1L);
        WaitingRequest request2 = new WaitingRequest(LocalDate.parse("2025-07-05"), 1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "어드민", "admin@email.com");

        //when
        WaitingResponse response = waitingService.save(request1, loginMember);

        //then
        assertThatThrownBy(() -> waitingService.save(request2, loginMember))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 예약 대기를 했습니다");
    }

    @Test
    void 예약_대기를_삭제한다() {
        //given
        WaitingRequest request = new WaitingRequest(LocalDate.parse("2025-07-05"), 1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "어드민", "admin@email.com");

        //when
        WaitingResponse response = waitingService.save(request, loginMember);
        waitingService.deleteById(response.id());

        //then
        assertThat(waitingService.findWaitingWithRankByMember(loginMember)).isEmpty();
    }

    @Test
    void 예약을_했으면_예약대기를_할_수_없다() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest(null, LocalDate.parse("2025-07-05"), 1L, 1L);
        WaitingRequest waitingRequest = new WaitingRequest(LocalDate.parse("2025-07-05"), 1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "어드민", "admin@email.com");
        reservationService.save(reservationRequest, loginMember);

        //then
        assertThatThrownBy(() -> waitingService.save(waitingRequest, loginMember))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 예약을 했습니다.");
    }
}
