package roomescape.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminInterceptor adminInterceptor;
    private final AuthenticationArgumentResolver authenticationArgumentResolver;

    public WebConfig(AdminInterceptor adminInterceptor, AuthenticationArgumentResolver authenticationArgumentResolver) {
        this.adminInterceptor = adminInterceptor;
        this.authenticationArgumentResolver = authenticationArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }
}

