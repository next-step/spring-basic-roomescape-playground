package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@DataJpaTest
class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;

    private String date = "2024-03-01";
    private Time time;
    private Theme theme;
    private Member member;

    @BeforeEach
    void setUp() {
        time = new Time("10:00");
        theme = new Theme("테마1", "테마1입니다.");
        member = new Member("admin", "admin@email.com", "password", "ADMIN");
        timeRepository.save(time);
        themeRepository.save(theme);
        memberRepository.save(member);
    }

    @Test
    void 해당_날짜_시간_테마에_대한_예약이_존재하지_않는_경우_조회_결과가_비어있다() {
        // when
        Optional<Reservation> savedReservation = reservationRepository.findWithMemberByDateAndTimeAndTheme(date, time,
                theme);

        // then
        assertThat(savedReservation).isEmpty();
    }

    @Test
    void 해당_날짜_시간_테마에_대한_예약이_존재_하는_경우_사용자와_함께_조회된다() {
        // given
        Reservation reservation = new Reservation(member.getName(), date, time, theme, member);
        reservationRepository.save(reservation);

        // when
        Optional<Reservation> savedReservation = reservationRepository.findWithMemberByDateAndTimeAndTheme(date, time,
                theme);

        // then
        assertThat(savedReservation).isNotEmpty();
        assertThat(savedReservation.get().getMember()).isEqualTo(member);
    }
}
