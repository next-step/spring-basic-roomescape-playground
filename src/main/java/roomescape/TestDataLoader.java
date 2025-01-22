package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Profile("test")
@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) throws Exception {
        Member adminMember = memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        Member userMember = memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));

        final Time time1 = timeRepository.save(new Time("10:00"));
        final Time time2 = timeRepository.save(new Time("12:00"));
        final Time time3 = timeRepository.save(new Time("14:00"));
        final Time time4 = timeRepository.save(new Time("16:00"));
        final Time time5 = timeRepository.save(new Time("18:00"));
        final Time time6 = timeRepository.save(new Time("20:00"));

        final Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        final Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        final Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        reservationRepository.save(new Reservation("어드민", "2024-03-01", time1, theme1, adminMember));
        reservationRepository.save(new Reservation("어드민", "2024-03-01", time2, theme2, adminMember));
        reservationRepository.save(new Reservation("어드민", "2024-03-01", time3, theme3, adminMember));

        reservationRepository.save(new Reservation("브라운", "2024-03-01", time1, theme2, userMember));
    }
}
