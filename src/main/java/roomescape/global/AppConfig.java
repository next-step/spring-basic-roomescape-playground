package roomescape.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import roomescape.auth.JwtProvider;

@Configuration
public class AppConfig {

    @Value("${JWT-Key}")
    private String secretKey;

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(secretKey);
    }
}
