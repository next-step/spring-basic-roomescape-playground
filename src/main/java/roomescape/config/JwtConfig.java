package roomescape.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jwt.JwtUtils;
import roomescape.member.MemberRepository;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public JwtUtils jwtUtil(MemberRepository memberRepo) {
        return new JwtUtils(secretKey, memberRepo);
    }
}
