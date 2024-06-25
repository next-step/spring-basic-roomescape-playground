package roomescape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminAccessInterceptor;
import roomescape.auth.AuthMemberArgumentResolver;
import roomescape.auth.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AuthMemberArgumentResolver authMemberArgumentResolver;
    private final AdminAccessInterceptor adminAccessInterceptor;

    @Autowired
    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver,
                     AuthMemberArgumentResolver authMemberArgumentResolver,
                     AdminAccessInterceptor adminAccessInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.authMemberArgumentResolver = authMemberArgumentResolver;
        this.adminAccessInterceptor = adminAccessInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
        resolvers.add(authMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAccessInterceptor)
                .addPathPatterns("/admin");
    }
}
