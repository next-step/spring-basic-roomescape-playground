package roomescape;

import jakarta.transaction.Transactional;
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
}
