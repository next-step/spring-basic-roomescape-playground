package auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import roomescape.domain.member.MemberRepository;

@Configuration
@ComponentScan(basePackages = {"roomescape", "auth"})
public class JwtAuthConfig {
    private final MemberRepository memberRepository;

    public JwtAuthConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public JwtAuthManager jwtAuthManager() {
        return new JwtAuthManager(memberRepository);
    }
}
