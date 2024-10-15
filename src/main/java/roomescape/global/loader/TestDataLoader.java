package roomescape.global.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import roomescape.global.exception.global.DataNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

@Profile("test")
@Component
public class TestDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public TestDataLoader(
        MemberRepository memberRepository,
        ReservationRepository reservationRepository,
        TimeRepository timeRepository,
        ThemeRepository themeRepository
    ) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
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

        reservationRepository.save(new Reservation(member, "", "2024-03-01", time1, theme1));
        reservationRepository.save(new Reservation(member, "", "2024-03-01", time2, theme2));
        reservationRepository.save(new Reservation(member, "", "2024-03-01", time3, theme3));
    }
}
