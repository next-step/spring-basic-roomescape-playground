package roomescape.initializer;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.Time;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

@Profile("test")
@Order(2)
@Component
public class TestDataInitializer implements CommandLineRunner {
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public TestDataInitializer(ThemeRepository themeRepository, TimeRepository timeRepository,
                               ReservationRepository reservationRepository, MemberRepository memberRepository) {
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        Time time1 = timeRepository.save(new Time("10:00"));
        Time time2 = timeRepository.save(new Time("12:00"));
        Time time3 = timeRepository.save(new Time("14:00"));
        Time time4 = timeRepository.save(new Time("16:00"));
        Time time5 = timeRepository.save(new Time("18:00"));
        Time time6 = timeRepository.save(new Time("20:00"));

        Member admin = memberRepository.findByEmail("admin@email.com")
                .orElseThrow(() -> new IllegalStateException("DataLoader에서 사용자가 초기화되지 않았습니다."));

        reservationRepository.save(
                new Reservation(admin, LocalDate.of(2024, 3, 1), time1, theme1)
        );
        reservationRepository.save(
                new Reservation(admin, LocalDate.of(2024, 3, 1), time2, theme2)
        );
        reservationRepository.save(
                new Reservation(admin, LocalDate.of(2024, 3, 1), time3, theme3)
        );

        reservationRepository.save(
                new Reservation("브라운", LocalDate.of(2024, 3, 1), time1, theme2)
        );
    }
}
