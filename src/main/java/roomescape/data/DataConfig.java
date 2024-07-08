package roomescape.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;

@Configuration
public class DataConfig {

    @Profile("default")
    @Bean
    CommandLineRunner dataLoader(MemberRepository memberRepository) {
        return new DataLoader(memberRepository);
    }

    @Profile("test")
    @Bean
    CommandLineRunner testDataLoader(
            MemberRepository memberRepository,
            TimeRepository timeRepository,
            ThemeRepository themeRepository,
            ReservationRepository reservationRepository
    ) {
        return new TestDataLoader(
                memberRepository,
                timeRepository,
                themeRepository,
                reservationRepository);
    }
}