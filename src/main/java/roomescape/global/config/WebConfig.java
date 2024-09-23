package roomescape.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.global.auth.AuthenticationInterceptor;
import roomescape.global.auth.LoginMemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AuthenticationInterceptor authenticationInterceptor;

    public WebConfig(
        LoginMemberArgumentResolver loginMemberArgumentResolver,
        AuthenticationInterceptor authenticationInterceptor
    ) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("/admin/**");
    }
}
