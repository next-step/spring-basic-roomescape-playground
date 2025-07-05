//package roomescape.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import roomescape.member.Member;
//import roomescape.member.MemberRepository;
//import roomescape.reservation.Reservation;
//import roomescape.reservation.ReservationRepository;
//import roomescape.theme.Theme;
//import roomescape.theme.ThemeRepository;
//import roomescape.time.Time;
//import roomescape.time.TimeRepository;
//
//import java.time.LocalDate;
//
//@Component
//public class DataInitializer {
//
//    private final MemberRepository memberRepository;
//    private final ThemeRepository themeRepository;
//    private final TimeRepository timeRepository;
//    private final ReservationRepository reservationRepository;
//
//    public DataInitializer(MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository,
//                           ReservationRepository reservationRepository) {
//        this.memberRepository = memberRepository;
//        this.themeRepository = themeRepository;
//        this.timeRepository = timeRepository;
//        this.reservationRepository = reservationRepository;
//    }
//
//    @PostConstruct
//    @Transactional
//    public void init() {
//        // Member 데이터
//        Member admin = memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
//        memberRepository.save(new Member("클로이", "chloe@email.com", "password", "USER"));
//        memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));
//
//        // Theme 데이터
//        Theme theme1 = themeRepository.save(new Theme("공포", "매우 무서운 테마", "https://i.imgur.com/1.jpg"));
//        Theme theme2 = themeRepository.save(new Theme("코믹", "매우 웃긴 테마", "https://i.imgur.com/2.jpg"));
//        Theme theme3 = themeRepository.save(new Theme("어드벤처", "신나는 모험 테마", "https://i.imgur.com/3.jpg"));
//
//        // Time 데이터
//        Time time1 = timeRepository.save(new Time("10:00"));
//        Time time2 = timeRepository.save(new Time("13:00"));
//        Time time3 = timeRepository.save(new Time("15:00"));
//
//        reservationRepository.save(new Reservation(LocalDate.parse("2024-03-01"), time1, theme1, admin));
//        reservationRepository.save(new Reservation(LocalDate.parse("2024-03-01"), time2, theme2, admin));
//        reservationRepository.save(new Reservation(LocalDate.parse("2024-03-01"), time3, theme3, admin));
//    }
//}
