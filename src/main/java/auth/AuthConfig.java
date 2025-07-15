package auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(secretKey);
    }
}
