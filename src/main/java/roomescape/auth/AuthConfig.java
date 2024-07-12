package roomescape.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
