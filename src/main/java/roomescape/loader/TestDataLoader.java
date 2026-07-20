package roomescape.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberRole;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationDao;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

import java.util.List;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {
    private final MemberDao memberDao;
    private final ThemeDao themeDao;
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TestDataLoader(MemberDao memberDao, ThemeDao themeDao, TimeDao timeDao,
                          ReservationDao reservationDao) {
        this.memberDao = memberDao;
        this.themeDao = themeDao;
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    @Override
    public void run(String... args) {
        List<Member> members = memberDao.saveAll(List.of(
                new Member("어드민", "admin@email.com", "password", MemberRole.ADMIN),
                new Member("브라운", "brown@email.com", "password", MemberRole.USER)
        ));
        List<Theme> themes = themeDao.saveAll(List.of(
                new Theme("테마1", "테마1입니다."),
                new Theme("테마2", "테마2입니다."),
                new Theme("테마3", "테마3입니다.")
        ));
        List<Time> times = timeDao.saveAll(List.of(
                new Time(null, "10:00"),
                new Time(null, "12:00"),
                new Time(null, "14:00"),
                new Time(null, "16:00"),
                new Time(null, "18:00"),
                new Time(null, "20:00")
        ));

        Member admin = members.get(0);
        reservationDao.saveAll(List.of(
                new Reservation("2024-03-01", admin, times.get(0), themes.get(0)),
                new Reservation("2024-03-01", admin, times.get(1), themes.get(1)),
                new Reservation("2024-03-01", admin, times.get(2), themes.get(2))
        ));
    }
}
