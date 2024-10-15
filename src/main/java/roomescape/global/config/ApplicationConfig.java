package roomescape.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import roomescape.global.auth.JwtUtils;

@Configuration
public class ApplicationConfig {

    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Bean
    public JwtUtils jwtProvider() {
        return new JwtUtils(secretKey);
    }
}
