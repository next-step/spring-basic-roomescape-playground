package roomescape.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.common.interceptor.AuthorizationInterceptor;
import roomescape.common.resolver.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, AuthorizationInterceptor authorizationInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authorizationInterceptor)
                .addPathPatterns("/admin/**");
    }
}
