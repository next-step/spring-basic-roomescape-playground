package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.Role;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    public TestDataLoader(MemberRepository memberRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository,
                          ReservationRepository reservationRepository,
                          PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Member admin = memberRepository.save(new Member("어드민", "admin@email.com", passwordEncoder.encode("password"), Role.ADMIN));
        Member user = memberRepository.save(new Member("브라운", "brown@email.com", passwordEncoder.encode("password"), Role.USER));

        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        Time t10 = timeRepository.save(new Time("10:00"));
        Time t12 = timeRepository.save(new Time("12:00"));
        Time t14 = timeRepository.save(new Time("14:00"));
        Time t16 = timeRepository.save(new Time("16:00"));
        Time t18 = timeRepository.save(new Time("18:00"));
        Time t20 = timeRepository.save(new Time("20:00"));

        reservationRepository.save(Reservation.memberReservation("2024-03-01", t10, theme1, admin));
        reservationRepository.save(Reservation.memberReservation("2024-03-01", t12, theme2, admin));
        reservationRepository.save(Reservation.memberReservation("2024-03-01", t14, theme3, admin));

    }
}
