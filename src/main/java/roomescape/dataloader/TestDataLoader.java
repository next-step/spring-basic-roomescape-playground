package roomescape.dataloader;

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

@Profile("test")
@Component
public class TestDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public TestDataLoader(MemberRepository memberRepository, ReservationRepository reservationRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Override
    public void run(String... args) {

        Member admin = memberRepository.save(
                new Member("어드민", "admin@email.com", "password", Role.ADMIN)
        );

        memberRepository.save(
                new Member("브라운", "brown@email.com", "password", Role.USER)
        );

        Theme theme = themeRepository.save(
                new Theme("테마1", "테마1입니다.")
        );

        Time time1 = timeRepository.save(new Time("10:00"));
        Time time2 = timeRepository.save(new Time("11:00"));
        Time time3 = timeRepository.save(new Time("13:00"));

        reservationRepository.save(
                new Reservation(
                        admin.getName(),
                        "2024-03-01",
                        time1,
                        theme,
                        admin
                )
        );

        reservationRepository.save(
                new Reservation(
                        admin.getName(),
                        "2024-03-02",
                        time2,
                        theme,
                        admin
                )
        );

        reservationRepository.save(
                new Reservation(
                        admin.getName(),
                        "2024-03-03",
                        time3,
                        theme,
                        admin
                )
        );
    }
}
