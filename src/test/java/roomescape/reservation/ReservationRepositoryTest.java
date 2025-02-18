package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 해당_날짜_시간_테마에_대한_본인의_예약이_존재하지_않는_경우_거짓을_반환한다() {
        // when
        boolean exists = reservationRepository.existsByDateAndTimeAndThemeAndMember(date, time, theme, member);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 해당_날짜_시간_테마에_대한_본인의_예약이_존재하는_경우_참을_반환한다() {
        // given
        Reservation reservation = new Reservation(member.getName(), date, time, theme, member);
        reservationRepository.save(reservation);

        // when
        boolean exists = reservationRepository.existsByDateAndTimeAndThemeAndMember(date, time, theme, member);

        // then
        assertThat(exists).isTrue();
    }
}
