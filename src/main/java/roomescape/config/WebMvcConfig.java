package roomescape.config;

import auth.JwtAuthManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import auth.AdminAccessInterceptor;
import auth.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"roomescape", "auth"})
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtAuthManager jwtAuthManager;

    public WebMvcConfig(JwtAuthManager jwtAuthManager) {
        this.jwtAuthManager = jwtAuthManager;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtAuthManager));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAccessInterceptor(jwtAuthManager))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/login");
    }
}
