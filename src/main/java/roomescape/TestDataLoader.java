package roomescape;

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
public class TestDataLoader implements CommandLineRunner {

    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public TestDataLoader(ThemeRepository themeRepository, TimeRepository timeRepository,
                          ReservationRepository reservationRepository, MemberRepository memberRepository) {
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String[] args) {
        // 테마 (id 1,2,3) — 저장 후 반환된 엔티티를 보관
        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        // 시간 (id 1~6)
        Time t1 = timeRepository.save(new Time("10:00"));
        Time t2 = timeRepository.save(new Time("12:00"));
        Time t3 = timeRepository.save(new Time("14:00"));
        timeRepository.save(new Time("16:00"));
        timeRepository.save(new Time("18:00"));
        timeRepository.save(new Time("20:00"));

        Member admin = memberRepository.save(new Member("어드민", "admin@email.com", "password", Role.ADMIN));
        Member brown = memberRepository.save(new Member("브라운", "brown@email.com", "password", Role.USER));

        // 예약 — name은 null (getName()이 회원 이름 반환). data.sql과 동일 매핑
        reservationRepository.save(new Reservation(null, admin, "2024-03-01", t1, theme1));
        reservationRepository.save(new Reservation(null, admin, "2024-03-01", t2, theme2));
        reservationRepository.save(new Reservation(null, admin, "2024-03-01", t3, theme3));
        reservationRepository.save(new Reservation(null, brown, "2024-03-01", t1, theme2));
    }
}