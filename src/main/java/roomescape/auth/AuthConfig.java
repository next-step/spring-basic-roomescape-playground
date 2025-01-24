package roomescape.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private static final String ADMIN_PATTERN = "/admin";
    private final AuthMemberArgumentResolver authMemberArgumentResolver;
    private final AuthInterceptor authInterceptor;

    public AuthConfig(
            @Autowired AuthMemberArgumentResolver authMemberArgumentResolver,
            @Autowired AuthInterceptor authInterceptor
    ) {
        this.authMemberArgumentResolver = authMemberArgumentResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(ADMIN_PATTERN);
    }
}
