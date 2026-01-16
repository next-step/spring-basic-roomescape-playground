package roomescape;

import auth.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminAuthInterceptor;
import roomescape.auth.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtUtils jwtUtils;

    public WebConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtUtils));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthInterceptor(jwtUtils))
                .addPathPatterns("/admin", "/admin/**");
    }
}


