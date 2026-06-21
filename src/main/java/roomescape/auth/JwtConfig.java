package roomescape.auth;

import roomescape.auth.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class JwtConfig {

    @Value("${roomescape.auth.jwt.secret}")
    String secretKey;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(this.secretKey);
    }
}