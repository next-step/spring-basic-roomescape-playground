package roomescape.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public JwtProvider jwtProvider(
            @Value("${roomescape.auth.jwt.secret}") String secret,
            @Value("${roomescape.auth.jwt.expiration}") long expiration
    ) {
        return new JwtProvider(secret, expiration);
    }
}
