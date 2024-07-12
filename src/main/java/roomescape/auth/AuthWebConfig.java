package roomescape.auth;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {
    private final AuthMemberArgumentResolver authMemberArgumentResolver;
    private final AuthAdminInteceptor authAdminInteceptor;

    public AuthWebConfig(AuthMemberArgumentResolver authMemberArgumentResolver, AuthAdminInteceptor authAdminInteceptor) {
        this.authMemberArgumentResolver = authMemberArgumentResolver;
        this.authAdminInteceptor = authAdminInteceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authAdminInteceptor).addPathPatterns("/admin");
    }
}
