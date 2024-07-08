package roomescape.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public JwtUtils jwtUtils(@Value("${roomescape.auth.jwt.secret}") String secretKey,
                             @Value("${roomescape.auth.jwt.expiration}") String expiration) {
        return new JwtUtils(secretKey, expiration);
    }
}
