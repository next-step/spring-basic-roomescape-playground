package jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public JWTUtils jwtUtils() {
        return new JWTUtils();
    }
}
