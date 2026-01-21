package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.Role;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TestDataLoader(MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) {
        Member admin = memberRepository.findByEmailAndPassword("admin@email.com", "password")
                .orElseGet(() -> memberRepository.save(new Member("어드민", "admin@email.com", "password", Role.ADMIN)));
        memberRepository.findByEmailAndPassword("brown@email.com", "password")
                .orElseGet(() -> memberRepository.save(new Member("브라운", "brown@email.com", "password", Role.USER)));

        if (themeRepository.count() == 0) {
            themeRepository.saveAll(List.of(
                    new Theme("테마1", "테마1입니다."),
                    new Theme("테마2", "테마2입니다."),
                    new Theme("테마3", "테마3입니다.")
            ));
        }

        if (timeRepository.count() == 0) {
            timeRepository.saveAll(List.of(
                    new Time("10:00"),
                    new Time("12:00"),
                    new Time("14:00"),
                    new Time("16:00"),
                    new Time("18:00"),
                    new Time("20:00")
            ));
        }

        if (reservationRepository.count() == 0) {
            List<Time> times = timeRepository.findAll();
            List<Theme> themes = themeRepository.findAll();
            if (times.size() >= 3 && themes.size() >= 3) {
                Reservation r1 = new Reservation("", "2024-03-01", times.get(0), themes.get(0));
                Reservation r2 = new Reservation("", "2024-03-01", times.get(1), themes.get(1));
                Reservation r3 = new Reservation("", "2024-03-01", times.get(2), themes.get(2));
                r1.setMember(admin);
                r2.setMember(admin);
                r3.setMember(admin);
                reservationRepository.saveAll(List.of(r1, r2, r3));
            }
        }
    }
}
