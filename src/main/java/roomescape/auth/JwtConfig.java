package roomescape.auth;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}