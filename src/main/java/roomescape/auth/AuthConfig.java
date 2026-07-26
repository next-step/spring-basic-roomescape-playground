package roomescape.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class AuthConfig {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Bean
    public JwtUtils jwtUtils() {
        // 1시간(3600000ms) 만료 기준
        return new JwtUtils(secretKey, 3600000L);
    }
}
