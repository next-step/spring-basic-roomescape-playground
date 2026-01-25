package auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long expiration;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(secretKey, expiration);
    }
}
