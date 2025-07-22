package roomescape.config;

import auth.JwtUtilsV4;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public JwtUtilsV4 jwtUtils(@Value("${roomescape.auth.jwt.secret}") String seceretKey,
                               @Value("${roomescape.auth.jwt.issuer}") String issuer) {
        return new JwtUtilsV4(seceretKey, issuer);
    }

}
