package roomescape.dataloader;

import org.springframework.boot.CommandLineRunner;
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

import java.time.LocalDate;

@Component
@Transactional
public class TestDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public TestDataLoader(MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Member member1 = new Member("어드민", "admin@email.com", "password", Role.ADMIN);
        Member member2 = new Member("브라운", "brown@email.com", "password", Role.USER);

        memberRepository.save(member1);
        memberRepository.save(member2);

        Theme theme1 = new Theme("테마1", "테마1입니다.");
        Theme theme2 = new Theme("테마2", "테마2입니다.");
        Theme theme3 = new Theme("테마3", "테마3입니다.");

        themeRepository.save(theme1);
        themeRepository.save(theme2);
        themeRepository.save(theme3);

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

        Reservation reservation1 = new Reservation("", LocalDate.parse("2024-03-01"), time1, theme1, member1);
        Reservation reservation2 = new Reservation("", LocalDate.parse("2024-03-01"), time2, theme2, member1);
        Reservation reservation3 = new Reservation("", LocalDate.parse("2024-03-01"), time3, theme3, member1);
        Reservation reservation4 = new Reservation("브라운", LocalDate.parse("2024-03-01"), time1, theme2, member2);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);
    }
}

//INSERT INTO member (name, email, password, role)
//VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
//       ('브라운', 'brown@email.com', 'password', 'USER');
//
//INSERT INTO theme (name, description)
//VALUES ('테마1', '테마1입니다.'),
//       ('테마2', '테마2입니다.'),
//               ('테마3', '테마3입니다.');
//
//INSERT INTO time (time_value)
//VALUES ('10:00'),
//       ('12:00'),
//               ('14:00'),
//               ('16:00'),
//               ('18:00'),
//               ('20:00');
//
//INSERT INTO reservation (member_id, name, date, time_id, theme_id)
//VALUES (1, '', '2024-03-01', 1, 1),
//       (1, '', '2024-03-01', 2, 2),
//               (1, '', '2024-03-01', 3, 3);
//
//INSERT INTO reservation (name, date, time_id, theme_id)
//VALUES ('브라운', '2024-03-01', 1, 2);

