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

@Profile("test") //테스트 환경에서만 동작
@Component
public class TestDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TestDataLoader(MemberRepository memberRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository,
                          ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. 회원 저장 (1L: 어드민, 2L: 브라운)
        Member admin = memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        Member brown = memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));

        // 2. 테마 저장
        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        // 3. 시간 저장
        Time time1 = timeRepository.save(new Time("10:00"));
        Time time2 = timeRepository.save(new Time("12:00"));
        Time time3 = timeRepository.save(new Time("14:00"));

        // 4. 5단계, 6단계 테스트에서 요구하는 예약 데이터 저장
        reservationRepository.save(new Reservation("어드민", "2024-03-01", time1, theme1, admin));
        reservationRepository.save(new Reservation("어드민", "2024-03-01", time1, theme2, admin));
        reservationRepository.save(new Reservation("어드민", "2024-03-01", time3, theme3, admin));
    }
}
