package roomescape.fixture;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import roomescape.member.repository.MemberRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.TimeRepository;

@TestConfiguration
public class FixtureConfig {

    @Bean
    public FixtureGenerator fixtureGenerator(MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        return new FixtureGenerator(memberRepository,timeRepository,themeRepository);
    }
}
