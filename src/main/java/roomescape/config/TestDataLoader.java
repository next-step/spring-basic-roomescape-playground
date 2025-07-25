package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepo;
    private final ThemeRepository themeRepo;
    private final TimeRepository timeRepo;
    private final ReservationRepository reservationRepo;

    public TestDataLoader(MemberRepository memberRepo,
                          ThemeRepository themeRepo,
                          TimeRepository timeRepo,
                          ReservationRepository reservationRepo) {
        this.memberRepo = memberRepo;
        this.themeRepo = themeRepo;
        this.timeRepo = timeRepo;
        this.reservationRepo = reservationRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Member admin = memberRepo.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        Member brown = memberRepo.save(new Member("브라운", "brown@email.com", "password", "USER"));

        Theme t1 = themeRepo.save(new Theme("테마1", "테마1입니다."));
        Theme t2 = themeRepo.save(new Theme("테마2", "테마2입니다."));
        Theme t3 = themeRepo.save(new Theme("테마3", "테마3입니다."));

        Time ti1 = timeRepo.save(new Time("10:00"));
        Time ti2 = timeRepo.save(new Time("12:00"));
        Time ti3 = timeRepo.save(new Time("14:00"));
        Time ti4 = timeRepo.save(new Time("16:00"));
        Time ti5 = timeRepo.save(new Time("18:00"));
        Time ti6 = timeRepo.save(new Time("20:00"));


        reservationRepo.save(new Reservation("2024-03-01", brown, t2, ti1));

        reservationRepo.save(new Reservation("2024-03-01", admin, t1, ti1));
        reservationRepo.save(new Reservation("2024-03-01", admin, t2, ti2));
        reservationRepo.save(new Reservation("2024-03-01", admin, t3, ti3));
    }
}
