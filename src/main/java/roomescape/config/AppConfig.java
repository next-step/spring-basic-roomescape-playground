package roomescape.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import auth.JwtProvider;

@Configuration
public class AppConfig {

    @Bean
    public JwtProvider jwtProvider(
        @Value("${security.jwt.token.secret-key}") String secretKey,
        @Value("${security.jwt.token.expire-length}") Long validityInMilliseconds
    ) {
        return new JwtProvider(secretKey, validityInMilliseconds);
    }
}
