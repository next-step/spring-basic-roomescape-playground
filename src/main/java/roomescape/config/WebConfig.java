package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.AdminHandlerInterceptor;
import roomescape.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminHandlerInterceptor adminHandlerInterceptor;


    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, AdminHandlerInterceptor adminHandlerInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminHandlerInterceptor = adminHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminHandlerInterceptor)
                .addPathPatterns("/admin");
    }
}
