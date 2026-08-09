package roomescape.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.web.CheckAdminInterceptor;
import roomescape.auth.web.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final CheckAdminInterceptor checkAdminInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, CheckAdminInterceptor checkAdminInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.checkAdminInterceptor = checkAdminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkAdminInterceptor)
                .addPathPatterns("/admin/**");
    }
}
