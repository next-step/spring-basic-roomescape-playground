package roomescape.auth;

import roomescape.auth.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtils jwtUtils(@Value("${jwt.secret}") String secretKey) {
        return new JwtUtils(secretKey);
    }
}