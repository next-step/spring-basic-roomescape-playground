package roomescape;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminInterceptor;
import roomescape.auth.AuthService;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.auth.TokenExtractor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final TokenExtractor tokenExtractor;

    public WebConfig(AuthService authService, TokenExtractor tokenExtractor) {
        this.authService = authService;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(authService, tokenExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(authService, tokenExtractor))
                .addPathPatterns("/admin", "/admin/**");
    }
}
