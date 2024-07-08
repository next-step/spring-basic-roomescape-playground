package auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    JwtUtils jwtTokenManager() {
        return new JwtUtils();
    }
}
