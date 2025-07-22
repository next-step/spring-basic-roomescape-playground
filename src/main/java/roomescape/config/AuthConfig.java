package roomescape.config;

import jwt.JwtProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jwt.JwtProperties;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class AuthConfig {

    private final JwtProperties jwtProperties;

    public AuthConfig(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(jwtProperties);
    }
}
