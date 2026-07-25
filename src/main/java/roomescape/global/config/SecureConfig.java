package roomescape.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.auth.jwt.JwtTokenProvider;

@Configuration
public class SecureConfig {

    @Value("${roomescape.auth.jwt.secret-key}")
    String secretKey;

    @Value("${roomescape.auth.jwt.access-token-expiration}")
    long accessTokenExpiration;

    @Value("${roomescape.auth.jwt.refresh-token-expiration}")
    long refreshTokenExpiration;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, accessTokenExpiration, refreshTokenExpiration);
    }
}
