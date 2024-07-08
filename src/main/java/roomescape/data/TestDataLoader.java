package roomescape.data;

import org.springframework.boot.CommandLineRunner;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

public class TestDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public TestDataLoader(final MemberRepository memberRepository,
                          final TimeRepository timeRepository,
                          final ThemeRepository themeRepository,
                          final ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        System.out.println("테스트 데이터 로더 실행");
        
        final Member member1 = memberRepository.save(
                new Member("어드민", "admin@email.com", "password", "ADMIN"));
        final Member member2 = memberRepository.save(
                new Member("브라운", "brown@email.com", "password", "USER"));

        final Theme theme1 = themeRepository.save(new Theme("테마1", "테마1입니다."));
        final Theme theme2 = themeRepository.save(new Theme("테마2", "테마2입니다."));
        final Theme theme3 = themeRepository.save(new Theme("테마3", "테마3입니다."));

        final Time time1 = timeRepository.save(new Time("10:00"));
        final Time time2 = timeRepository.save(new Time("12:00"));
        final Time time3 = timeRepository.save(new Time("14:00"));
        final Time time4 = timeRepository.save(new Time("16:00"));
        final Time time5 = timeRepository.save(new Time("18:00"));
        final Time time6 = timeRepository.save(new Time("20:00"));

        reservationRepository.save(new Reservation("어드민", "2024-03-01", time1, theme1, member1));
        reservationRepository.save(new Reservation("어드민", "2024-03-01", time2, theme2, member1));
        reservationRepository.save(new Reservation("어드민", "2024-03-01", time3, theme3, member1));

        reservationRepository.save(new Reservation("브라운", "2024-03-01", time4, theme1, member2));
    }
}
