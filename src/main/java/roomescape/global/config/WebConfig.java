package roomescape.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.domain.auth.web.support.LoginMemberArgumentResolver;
import roomescape.domain.auth.web.support.RoleInterceptor;
import roomescape.domain.auth.web.support.SessionManager;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] adminAllowedOrigins = {
            "/admin",
            "/admin/**",

    };

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final SessionManager sessionManager;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, SessionManager sessionManager) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.sessionManager = sessionManager;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor(sessionManager));
    }
}
