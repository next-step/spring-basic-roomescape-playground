package roomescape.infrastructure;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public DataInitializer(
            MemberRepository memberRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            ReservationRepository reservationRepository
    ) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. 관리자 계정 생성
        if (memberRepository.findByEmail("admin").isEmpty()) {
            Member admin = new Member("admin", "admin", "admin", "ADMIN");
            memberRepository.save(admin);
            System.out.println("관리자 계정이 생성되었습니다.");
        }

        // 2. 테마 데이터 생성
        if (themeRepository.count() == 0) {
            Theme theme1 = new Theme("테마1", "테마1입니다.");
            Theme theme2 = new Theme("테마2", "테마2입니다.");
            Theme theme3 = new Theme("테마3", "테마3입니다.");

            themeRepository.save(theme1);
            themeRepository.save(theme2);
            themeRepository.save(theme3);
            System.out.println("테마 데이터가 생성되었습니다.");
        }

        // 3. 시간 데이터 생성
        if (timeRepository.count() == 0) {
            Time time1 = new Time("10:00");
            Time time2 = new Time("12:00");
            Time time3 = new Time("14:00");
            Time time4 = new Time("16:00");
            Time time5 = new Time("18:00");
            Time time6 = new Time("20:00");

            timeRepository.save(time1);
            timeRepository.save(time2);
            timeRepository.save(time3);
            timeRepository.save(time4);
            timeRepository.save(time5);
            timeRepository.save(time6);
            System.out.println("시간 데이터가 생성되었습니다.");
        }

        if (reservationRepository.count() == 0) {
            Member admin = memberRepository.findByEmail("admin")
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            Time time1 = timeRepository.findById(1L).orElseThrow();
            Time time2 = timeRepository.findById(2L).orElseThrow();
            Time time3 = timeRepository.findById(3L).orElseThrow();

            Theme theme1 = themeRepository.findById(1L).orElseThrow();
            Theme theme2 = themeRepository.findById(2L).orElseThrow();
            Theme theme3 = themeRepository.findById(3L).orElseThrow();

            Reservation reservation1 = new Reservation("", "2024-03-01", time1, theme1, admin);
            Reservation reservation2 = new Reservation("", "2024-03-01", time2, theme2, admin);
            Reservation reservation3 = new Reservation("", "2024-03-01", time3, theme3, admin);

            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
            reservationRepository.save(reservation3);

            Reservation reservation4 = new Reservation("브라운", "2024-03-01", time1, theme2);

            reservationRepository.save(reservation4);
            System.out.println("예약 데이터가 생성되었습니다.");
        }

        System.out.println("초기 데이터 로딩이 완료되었습니다.");
    }
}