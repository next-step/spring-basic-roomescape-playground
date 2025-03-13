package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AuthService;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.auth.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public WebConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(authService))
                .addPathPatterns("/admin/**")
                .order(1);
    }
}
