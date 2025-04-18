package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.enums.Role;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @DisplayName("예약_삭제_시_대기자가_있으면_예약으로_등록된다")
    @Test
    void givenReservationsDeleteWhenWaitingExistedThenAutoReserve() {
        // given
        Member reservingMember = memberRepository.save(new Member("철수", "chul@example.com", "1234", Role.USER));
        Member waitingMember = memberRepository.save(new Member("영희", "young@example.com", "abcd", Role.USER));
        Theme theme = themeRepository.save(new Theme("호러룸", "무서운 방"));
        Time time = timeRepository.save(new Time("12:00"));
        String date = "2025-04-20";

        Reservation reservation = reservationRepository.save(new Reservation(
                reservingMember.getName(), date, time, theme, reservingMember));
        waitingRepository.save(new Waiting(theme, date, time, waitingMember));

        LoginMember loginMember = new LoginMember(reservingMember.getId(), reservingMember.getName(), reservingMember.getEmail(), reservingMember.getRole().name());

        // when
        reservationService.deleteById(reservation.getId(), loginMember);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        List<Waiting> waitings = waitingRepository.findAll();

        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getMember().getId()).isEqualTo(waitingMember.getId());
        assertThat(waitings).isEmpty();
    }

}
