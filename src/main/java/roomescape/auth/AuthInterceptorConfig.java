package roomescape.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthInterceptorConfig {

    private final AuthService authService;

    public AuthInterceptorConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(authService);
    }
}
