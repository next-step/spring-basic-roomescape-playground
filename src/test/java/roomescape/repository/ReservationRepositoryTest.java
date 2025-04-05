package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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

@DataJpaTest
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Test
    @DisplayName("예약을 저장하고 조회한다")
    void saveAndFindReservation() {
        // given
        Member member = memberRepository.save(new Member("홍길동", "exam@naver.com", "qwer", Role.USER));
        Theme theme = themeRepository.save(new Theme("공포의 방", "공포 테마"));
        Time time = timeRepository.save(new Time("14:00"));
        reservationRepository.save(new Reservation("2025-04-05", member, time, theme));

        // when
        List<Reservation> reservations = reservationRepository.findByDateAndTheme("2025-04-05", theme);

        // then
        assertThat(reservations).isNotEmpty();
        assertThat(reservations.get(0).getMember().getName()).isEqualTo("홍길동");
    }
}
