package auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "auth")
public class AuthConfig {
    @Bean
    public JwtUtils jwtUtils(@Value("${roomescape.auth.jwt.secret}") String jwtSecret) {
        return new JwtUtils(jwtSecret);
    }
}
