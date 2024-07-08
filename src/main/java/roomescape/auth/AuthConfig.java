package roomescape.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.api.JwtUtils;

@Configuration
public class AuthConfig {
    @Bean
    public JwtUtils jwtProvider(
            @Value("${roomescape.auth.jwt.secret}")
            String secretKey
            ) {
        return new JwtUtils(secretKey);
    }
}
