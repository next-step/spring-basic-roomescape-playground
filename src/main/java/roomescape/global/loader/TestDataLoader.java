package roomescape.global.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import roomescape.member.model.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public TestDataLoader(ReservationRepository reservationRepository, MemberRepository memberRepository,
        ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        Time time1 = timeRepository.save(new Time("10:00"));
        Time time2 = timeRepository.save(new Time("12:00"));
        Time time3 = timeRepository.save(new Time("14:00"));
        Time time4 = timeRepository.save(new Time("16:00"));
        Time time5 = timeRepository.save(new Time("18:00"));
        Time time6 = timeRepository.save(new Time("20:00"));

        Member member = memberRepository.getById(1L);

        reservationRepository.save(new Reservation("2024-03-01", member, time1, theme1));
        reservationRepository.save(new Reservation("2024-03-01", member, time2, theme2));
        reservationRepository.save(new Reservation("2024-03-01", member, time3, theme3));
    }
}
