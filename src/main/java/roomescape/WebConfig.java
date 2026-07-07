package roomescape;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminOnlyInterceptor;
import roomescape.auth.AuthUserArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthUserArgumentResolver authUserArgumentResolver;
    private final AdminOnlyInterceptor adminOnlyInterceptor;

    public WebConfig(AuthUserArgumentResolver authUserArgumentResolver, AdminOnlyInterceptor adminOnlyInterceptor) {
        this.authUserArgumentResolver = authUserArgumentResolver;
        this.adminOnlyInterceptor = adminOnlyInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminOnlyInterceptor);
    }
}
