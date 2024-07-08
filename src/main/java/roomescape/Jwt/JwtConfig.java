package roomescape.Jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.Jwt.JwtUtils;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
