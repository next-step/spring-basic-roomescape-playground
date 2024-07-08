package roomescape;

import org.springframework.boot.CommandLineRunner;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

public class TestDataLoader implements CommandLineRunner {

    private MemberRepository memberRepository;
    private ThemeRepository themeRepository;
    private TimeRepository timeRepository;
    private ReservationRepository reservationRepository;

    public TestDataLoader(MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        final Member member1 = memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        final Member member2 = memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));

        final Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        final Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        final Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        final Time time1 = timeRepository.save(new Time("10:00"));
        final Time time2 = timeRepository.save(new Time("12:00"));
        final Time time3 = timeRepository.save(new Time("14:00"));
        final Time time4 = timeRepository.save(new Time("16:00"));
        final Time time5 = timeRepository.save(new Time("18:00"));
        final Time time6 = timeRepository.save(new Time("20:00"));

        final Reservation reservation1 = reservationRepository.save(new Reservation("어드민", "2024-03-01", time1, theme1));
        final Reservation reservation2 = reservationRepository.save(new Reservation("어드민", "2024-03-01", time2, theme2));
        final Reservation reservation3 = reservationRepository.save(new Reservation("어드민", "2024-03-01", time3, theme3));
        final Reservation reservation4 = reservationRepository.save(new Reservation("브라운", "2024-03-01", time4, theme1));

    }
}
