package roomescape;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminPageAuthorizeInterceptor;
import roomescape.auth.AuthorizedMemberArgumentResolver;

@Configuration
public class RoomescapeWebMvcConfigurer implements WebMvcConfigurer {
    private final AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver;
    private final AdminPageAuthorizeInterceptor adminPageAuthorizeInterceptor;

    public RoomescapeWebMvcConfigurer(
            AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver,
            AdminPageAuthorizeInterceptor adminPageAuthorizeInterceptor
    ) {
        this.authorizedMemberArgumentResolver = authorizedMemberArgumentResolver;
        this.adminPageAuthorizeInterceptor = adminPageAuthorizeInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizedMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminPageAuthorizeInterceptor)
                .addPathPatterns("/admin/**");
    }
}
