package roomescape.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.auth.jwt.JwtUtils;

@Configuration
public class AuthConfig {

    @Bean
    public JwtUtils jwtUtils(@Value("${roomescape.auth.jwt.secret}") String jwtSecret,
                             @Value("${roomescape.auth.jwt.expiration}") Long expireMilliseconds) {
        return new JwtUtils(jwtSecret, expireMilliseconds);
    }
}
