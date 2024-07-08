package auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.member.MemberRepository;

@Configuration
public class JwtUtilsConfig {
    private final MemberRepository memberRepository;

    public JwtUtilsConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(memberRepository);
    }
}
