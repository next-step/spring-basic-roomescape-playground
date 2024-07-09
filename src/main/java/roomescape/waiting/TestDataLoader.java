package roomescape.waiting;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {

    private final MemberDao memberDao;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TestDataLoader(MemberDao memberDao, ThemeRepository themeRepository,
                          TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.memberDao = memberDao;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Member admin = memberDao.existsByEmail("admin@email.com") ?
                memberDao.findByEmail("admin@email.com") :
                memberDao.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));

        Member brown = memberDao.existsByEmail("brown@email.com") ?
                memberDao.findByEmail("brown@email.com") :
                memberDao.save(new Member("브라운", "brown@email.com", "password", "USER"));

        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        Time time1 = timeRepository.save(new Time("10:00"));
        Time time2 = timeRepository.save(new Time("12:00"));
        Time time3 = timeRepository.save(new Time("14:00"));
        Time time4 = timeRepository.save(new Time("16:00"));
        Time time5 = timeRepository.save(new Time("18:00"));
        Time time6 = timeRepository.save(new Time("20:00"));

        reservationRepository.save(new Reservation(admin.getId(), "2024-03-01", theme1.getId(), time1.getId()));
        reservationRepository.save(new Reservation(admin.getId(), "2024-03-01", theme2.getId(), time2.getId()));
        reservationRepository.save(new Reservation(admin.getId(), "2024-03-01", theme3.getId(), time3.getId()));
        reservationRepository.save(new Reservation(brown.getId(), "2024-03-01", theme2.getId(), time1.getId()));
    }
}
