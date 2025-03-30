package roomescape.waiting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.dto.request.WaitingRequest;
import roomescape.waiting.dto.response.WaitingResponse;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WaitingServiceTest {

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 예약_대기를_신청할_수_있다() {
        // given
        Member member = createMember("멤버", "member@email.com");
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());

        LocalDate date = LocalDate.of(2025, 3, 30);
        Time time = createTime();
        Theme theme = createTheme();
        WaitingRequest request = new WaitingRequest(date, time.getId(), theme.getId());

        int expectedWaitingNumber = 1;
        // when
        WaitingResponse response = waitingService.createWaiting(request, loginMember);
        // then
        assertThat(response.waitingNumber()).isEqualTo(expectedWaitingNumber);
    }

    @Test
    void 이미_예약을_한_상태에서_예약_대기를_신청하면_예외가_발생한다() {
        // given
        Member member = createMember("멤버", "member@email.com");
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());

        LocalDate date = LocalDate.of(2025, 3, 30);
        Time time = createTime();
        Theme theme = createTheme();

        Reservation reservation = new Reservation(member.getId(), member.getName(), date, time, theme);
        reservationRepository.save(reservation);

        WaitingRequest request = new WaitingRequest(date, time.getId(), theme.getId());
        // when & then
        assertThatThrownBy(() -> waitingService.createWaiting(request, loginMember))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.RESERVATION_ALREADY_EXISTS.getMessage());
    }

    @Test
    void 예약_대기를_중복으로_신청하면_예외가_발생한다() {
        // given
        Member member = createMember("멤버", "member@email.com");
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());

        LocalDate date = LocalDate.of(2025, 3, 30);
        Time time = createTime();
        Theme theme = createTheme();

        WaitingRequest request = new WaitingRequest(date, time.getId(), theme.getId());
        waitingService.createWaiting(request, loginMember);

        // when & then
        assertThatThrownBy(() -> waitingService.createWaiting(request, loginMember))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WAITING_ALREADY_EXISTS.getMessage());
    }

    private Theme createTheme() {
        Theme theme = new Theme("커스텀테마1", "커스텀테마 입니다.");
        return themeRepository.save(theme);
    }

    private Time createTime() {
        Time time = new Time(LocalTime.of(22, 0));
        return timeRepository.save(time);
    }

    private Member createMember(String name, String email) {
        Member member = new Member(name, email, "password", Role.USER);
        return memberRepository.save(member);
    }
}
