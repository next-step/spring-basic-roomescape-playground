package roomescape;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.reservation.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class WaitingTest {

    @Autowired
    private WaitingService waitingService;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("이미 대기를 신청한 것과 동일한 조건으로 중복 신청 시 예외가 발생한다.")
    void 동일한_조건의_대기_신청인지_확인_테스트() {
        Time time = timeRepository.save(new Time("10:00"));
        Theme theme = themeRepository.save(new Theme("방탈출 테마", "테마 설명"));
        Member member = memberRepository.save(new Member("test@email.com", "1234", "하은", "USER"));

        reservationRepository.save(new Reservation("하은", "2024-03-01", time, theme, member));
        ReservationRequest request = new ReservationRequest("하은", "2024-03-01", theme.getId(), time.getId());
        waitingService.createWaiting(request, member);

        assertThatThrownBy(() -> waitingService.createWaiting(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 대기를 신청한 타임입니다.");
    }

    @Test
    @DisplayName("예약이 이미 없는 타임에 대기 신청 시 예외가 발생한다.")
    void 예약이_존재하지_않는_타임_대기_신청_예외_테스트() {
        Time time = timeRepository.save(new Time("10:00"));
        Theme theme = themeRepository.save(new Theme("방탈출 테마", "테마 설명"));
        Member member = memberRepository.save(new Member("test@email.com", "1234", "하은", "USER"));

        ReservationRequest request = new ReservationRequest("하은", "2024-03-01", theme.getId(), time.getId());

        assertThatThrownBy(() -> waitingService.createWaiting(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약이 존재하지 않는 타임에는 대기를 신청할 수 없습니다.");
    }

    @Test
    @DisplayName("선행 예약이 존재하는 경우 정상적으로 대기를 신청한다.")
    void 정상_대기_신청_확인_테스트() {
        Time time = timeRepository.save(new Time("10:00"));
        Theme theme = themeRepository.save(new Theme("방탈출 테마", "테마 설명"));
        Member member = memberRepository.save(new Member("test@email.com", "1234", "하은", "USER"));

        reservationRepository.save(new Reservation("하은", "2024-03-01", time, theme, member));
        ReservationRequest request = new ReservationRequest("하은", "2024-03-01", theme.getId(), time.getId());

        WaitingResponse response = waitingService.createWaiting(request, member);

        assertThat(response).isNotNull();
    }
}
