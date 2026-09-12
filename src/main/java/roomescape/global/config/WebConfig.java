package roomescape.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.global.auth.jwt.JwtTokenProvider;
import roomescape.global.auth.LoginMemberArgumentResolver;
import roomescape.global.auth.RoleInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] adminAllowedOrigins = {
            "/admin"
    };

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver,  JwtTokenProvider jwtTokenProvider) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleInterceptor(jwtTokenProvider, "ADMIN"))
                .addPathPatterns(adminAllowedOrigins);
    }
}
