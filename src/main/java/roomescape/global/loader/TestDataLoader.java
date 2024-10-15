package roomescape.global.loader;

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

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {

    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public TestDataLoader(
        ThemeRepository themeRepository,
        TimeRepository timeRepository,
        ReservationRepository reservationRepository,
        MemberRepository memberRepository
    ) {
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        Time time1 = timeRepository.save(new Time("10:00"));
        Time time2 = timeRepository.save(new Time("12:00"));
        Time time3 = timeRepository.save(new Time("14:00"));
        Time time4 = timeRepository.save(new Time("16:00"));
        Time time5 = timeRepository.save(new Time("18:00"));
        Time time6 = timeRepository.save(new Time("20:00"));

        Member member1 = memberRepository.getById(1L);

        reservationRepository.save(new Reservation("", "2024-03-01", member1, time1, theme1));
        reservationRepository.save(new Reservation("", "2024-03-01", member1, time2, theme2));
        reservationRepository.save(new Reservation("", "2024-03-01", member1, time3, theme3));

        reservationRepository.save(new Reservation("브라운", "2024-03-01", null, time1, theme2));
    }
}
