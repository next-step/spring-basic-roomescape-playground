package roomescape;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminPageAuthorizeInterceptor;
import roomescape.auth.AuthenticationInterceptor;
import roomescape.auth.AuthorizationInterceptor;
import roomescape.auth.AuthorizedMemberArgumentResolver;

@Configuration
public class RoomescapeWebMvcConfigurer implements WebMvcConfigurer {
    private final AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver;
    private final AdminPageAuthorizeInterceptor adminPageAuthorizeInterceptor;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    public RoomescapeWebMvcConfigurer(
            AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver,
            AdminPageAuthorizeInterceptor adminPageAuthorizeInterceptor,
            AuthenticationInterceptor authenticationInterceptor,
            AuthorizationInterceptor authorizationInterceptor
    ) {
        this.authorizedMemberArgumentResolver = authorizedMemberArgumentResolver;
        this.adminPageAuthorizeInterceptor = adminPageAuthorizeInterceptor;
        this.authenticationInterceptor = authenticationInterceptor;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizedMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                        .order(-100);

        registry.addInterceptor(authorizationInterceptor);

        registry.addInterceptor(adminPageAuthorizeInterceptor)
                .addPathPatterns("/admin/**");
    }
}
