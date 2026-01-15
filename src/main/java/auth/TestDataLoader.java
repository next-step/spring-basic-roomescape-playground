package auth;

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
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TestDataLoader(MemberRepository memberRepository, ThemeRepository themeRepository,
                          TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) {
        Member admin = memberRepository.save(new Member("어드민", "admin@email.com", "password", Role.ADMIN));
        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Time time1 = timeRepository.save(new Time("10:00"));

        reservationRepository.save(new Reservation(admin, "2024-03-01", time1, theme1));
    }
}
