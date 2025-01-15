package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.time.TimeRepository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;

@Profile("test")
@Component
public abstract class TestDataLoader implements CommandLineRunner {
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
    @Transactional
    public void run(String... args) throws Exception {
        Member admin = new Member("어드민", "admin@email.com", "password", "ADMIN");
        Member brown = new Member("브라운", "brown@email.com", "password", "USER");
        memberRepository.save(admin);
        memberRepository.save(brown);

        Theme theme1 = new Theme("테마1", "테마1입니다.");
        Theme theme2 = new Theme("테마2", "테마2입니다.");
        themeRepository.save(theme1);
        themeRepository.save(theme2);

        Time time1 = new Time("10:00");
        Time time2 = new Time("12:00");
        timeRepository.save(time1);
        timeRepository.save(time2);

        Reservation reservation1 = new Reservation("어드민", "2024-03-01", time1, theme1, admin);
        Reservation reservation2 = new Reservation("브라운", "2024-03-01", time2, theme2, brown);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        System.out.println("테스트 데이터가 초기화되었습니다.");
    }
}
