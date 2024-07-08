package roomescape.global.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.AdminHandlerInterceptor;
import roomescape.global.LoginMemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminHandlerInterceptor adminHandlerInterceptor;

    public WebConfig(final LoginMemberArgumentResolver loginMemberArgumentResolver,
                     final AdminHandlerInterceptor adminHandlerInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminHandlerInterceptor = adminHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminHandlerInterceptor)
                .addPathPatterns("/admin/**");
    }
}
