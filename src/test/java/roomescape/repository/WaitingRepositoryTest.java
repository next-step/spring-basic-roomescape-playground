package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.member.Role;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.repository.WaitingRepository;

@DataJpaTest
public class WaitingRepositoryTest {
    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("대기열 추가 및 조회 테스트")
    void saveAndFindWaiting() {
        // given
        Member member = memberRepository.save(new Member("홍길동", "example@naver.com", "qwer", Role.USER));
        Theme theme = themeRepository.save(new Theme("미스터리 방", "추리 테마"));
        Time time = timeRepository.save(new Time("16:00"));
        Reservation reservation = reservationRepository.save(new Reservation("2025-04-10", member, time, theme));

        Waiting waiting = new Waiting("2025-04-10", time.getValue(), theme, member, reservation);
        waitingRepository.save(waiting);

        // when
        List<Waiting> waitings = waitingRepository.findAllByThemeIdAndDateAndTime(
                theme.getId(), "2025-04-10", time.getValue());

        // then
        assertThat(waitings).hasSize(1);
        assertThat(waitings.get(0).getMember().getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("대기열 중 가장 오래된 항목 조회")
    void findTopByReservationOrderByCreatedDateTime() {
        // given
        Member member = memberRepository.save(new Member("홍길동", "example@naver.com", "qwer", Role.USER));
        Theme theme = themeRepository.save(new Theme("스릴러 방", "스릴러 테마"));
        Time time = timeRepository.save(new Time("18:00"));
        Reservation reservation = reservationRepository.save(new Reservation("2025-04-12", member, time, theme));

        Waiting waiting = new Waiting("2025-04-12", time.getValue(), theme, member, reservation);
        waitingRepository.save(waiting);

        // when
        Optional<Waiting> foundWaiting = waitingRepository.findTopByReservationOrderByCreatedDateTime(reservation);

        // then
        assertThat(foundWaiting).isPresent();
        assertThat(foundWaiting.get().getMember().getName()).isEqualTo("홍길동");
    }
}
