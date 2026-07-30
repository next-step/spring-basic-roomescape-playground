package roomescape;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.LoginMember;
import roomescape.exception.ForbiddenException;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.member.Role;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.reservation.ReservationService;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private TimeRepository timeRepository;

    private Member member;
    private Theme theme;
    private Time time;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        member = new Member("대현", "test@test.com", "1234", Role.USER);
        theme = new Theme("공포", "무서운 테마");
        time = new Time("10:00");
        loginMember = new LoginMember(1L, "대현", Role.USER);
    }

    @Test
    void 예약을_생성한다() {
        ReservationRequest request =
                new ReservationRequest(null, "2027-08-01", 1L, 1L);

        when(memberService.findById(1L)).thenReturn(member);
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        reservationService.save(request, loginMember);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_테마면_예외가_발생한다() {
        ReservationRequest request =
                new ReservationRequest(null, "2027-08-01", 1L, 1L);

        when(memberService.findById(1L)).thenReturn(member);
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                reservationService.save(request, loginMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @Test
    void 존재하지_않는_시간이면_예외가_발생한다() {
        ReservationRequest request =
                new ReservationRequest(null, "2027-08-01", 1L, 1L);

        when(memberService.findById(1L)).thenReturn(member);
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(timeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                reservationService.save(request, loginMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 시간입니다.");
    }

    @Test
    void 이미_예약된_일정이면_예외가_발생한다() {
        ReservationRequest request =
                new ReservationRequest(null, "2027-08-01", 1L, 1L);

        when(memberService.findById(1L)).thenReturn(member);

        when(reservationRepository.existsByDateAndTimeIdAndThemeId(
                "2027-08-01", 1L, 1L))
                .thenReturn(true);

        assertThatThrownBy(() ->
                reservationService.save(request, loginMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 일정입니다.");
    }

    @Test
    void 이름이_공백이면_예외가_발생한다() {
        ReservationRequest request =
                new ReservationRequest(" ", "2027-08-01", 1L, 1L);

        assertThatThrownBy(() ->
                reservationService.save(request, loginMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 공백일 수 없습니다.");
    }

    @Test
    void 다른_사용자의_이름으로_예약하면_예외가_발생한다() {
        ReservationRequest request =
                new ReservationRequest("철수", "2027-08-01", 1L, 1L);

        assertThatThrownBy(() ->
                reservationService.save(request, loginMember))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("다른 사용자의 이름으로 예약할 수 없습니다.");
    }

    @Test
    void 관리자는_다른_사용자의_이름으로_예약할_수_있다() {
        LoginMember admin =
                new LoginMember(1L, "관리자", Role.ADMIN);

        Member targetMember =
                new Member("철수", "test@test.com", "1234", Role.USER);

        ReservationRequest request =
                new ReservationRequest("철수", "2027-08-01", 1L, 1L);

        when(memberService.findByName("철수")).thenReturn(targetMember);
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(timeRepository.findById(1L)).thenReturn(Optional.of(time));

        reservationService.save(request, admin);

        verify(memberService).findByName("철수");
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void 과거시간으로는_예약을_할수없다() {
        ReservationRequest request =
                new ReservationRequest(null, "2024-08-01", 1L, 1L);

        assertThatThrownBy(() ->
                reservationService.save(request, loginMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("과거 날짜로는 예약할 수 없습니다.");
    }
}
