package roomescape.dataLoader;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Component
public class TestDataLoader implements CommandLineRunner {

    @Autowired
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
    public void run(String... args) {
        Member admin = memberRepository.findByName("ADMIN")
                .orElseThrow();
        Member brown = memberRepository.findByName("브라운")
                .orElseThrow();
        if (themeRepository.count() > 0) {
            return;
        }
        Theme theme1 = Theme.builder()
                .name("테마1")
                .description("테마1입니다.")
                .build();
        Theme theme2 = Theme.builder()
                .name("테마2")
                .description("테마2입니다.")
                .build();
        Theme theme3 = Theme.builder()
                .name("테마3")
                .description("테마3입니다.")
                .build();

        themeRepository.save(theme1);
        themeRepository.save(theme2);
        themeRepository.save(theme3);

        if (timeRepository.count() > 0) {
            return;
        }
        Time time1 = Time.builder()
                .value("10:00")
                .build();
        Time time2 = Time.builder()
                .value("12:00")
                .build();
        Time time3 = Time.builder()
                .value("14:00")
                .build();
        Time time4 = Time.builder()
                .value("16:00")
                .build();
        Time time5 = Time.builder()
                .value("18:00")
                .build();
        Time time6 = Time.builder()
                .value("20:00")
                .build();
        timeRepository.save(time1);
        timeRepository.save(time2);
        timeRepository.save(time3);
        timeRepository.save(time4);
        timeRepository.save(time5);
        timeRepository.save(time6);

        reservationRepository.save(
                Reservation.builder()
                        .member(admin)
                        .date("2024-03-01")
                        .time(time1)
                        .theme(theme1)
                        .build());
        reservationRepository.save(
                Reservation.builder()
                        .member(admin)
                        .date("2024-03-01")
                        .time(time2)
                        .theme(theme2)
                        .build());
        reservationRepository.save(
                Reservation.builder()
                        .member(brown)
                        .date("2024-03-01")
                        .time(time3)
                        .theme(theme3)
                        .build());
    }
}
