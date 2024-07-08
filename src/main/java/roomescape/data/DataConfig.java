package roomescape.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.repository.TimeRepository;

@Configuration
public class DataConfig {

    /**
     * @param memberRepository
     * 스프링 컨텍스트가 메서드의 파라미터로 붙은
     * MemberRepository 타입으로 등록된 빈이 있는지 조회한 후, 해당 인스턴스를 주입한다.
     */
    @Profile("prod")
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
