package roomescape.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthWebConfiguration implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver authMemberArgumentResolver;

    public AuthWebConfiguration(
            LoginMemberArgumentResolver authMemberArgumentResolver
    ) {
        this.authMemberArgumentResolver = authMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberArgumentResolver);
    }
}
