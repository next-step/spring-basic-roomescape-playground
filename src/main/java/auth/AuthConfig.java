package auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public JWTUtils jwtUtils() {
        return new JWTUtils();
    }

    @Bean
    public AuthClaimsArgumentResolver authClaimsArgumentResolver(JWTUtils jwtUtils) {
        return new AuthClaimsArgumentResolver(jwtUtils);
    }

    @Bean
    public AuthRoleInterceptor authRoleInterceptor(JWTUtils jwtUtils) {
        return new AuthRoleInterceptor(jwtUtils);
    }
}
