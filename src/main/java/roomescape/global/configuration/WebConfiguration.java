package roomescape.global.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AuthService;
import roomescape.auth.client.cookie.CookieResolver;
import roomescape.auth.client.jwt.JwtResolver;
import roomescape.global.interceptor.AuthInterceptor;
import roomescape.global.resolver.MemberArgumentResolver;
import roomescape.member.MemberService;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final AuthService authService;
    private final JwtResolver jwtResolver;
    private final CookieResolver cookieResolver;
    private final AuthInterceptor authInterceptor;

    public WebConfiguration(AuthService authService, JwtResolver jwtResolver,
                            CookieResolver cookieResolver,
                            AuthInterceptor authInterceptor) {
        this.authService = authService;
        this.jwtResolver = jwtResolver;
        this.cookieResolver = cookieResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> memberArgumentResolver) {
        memberArgumentResolver.add(new MemberArgumentResolver(authService, jwtResolver, cookieResolver));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/admin/**");
    }
}
