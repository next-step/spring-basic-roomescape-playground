package roomescape.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import roomescape.global.auth.JwtUtils;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration-time}")
    private Long expirationTime;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(secretKey, expirationTime);
    }
}
