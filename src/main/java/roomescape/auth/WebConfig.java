package roomescape.auth;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final RoleCheckHandlerInterceptor roleCheckHandlerInterceptor;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver,
                     RoleCheckHandlerInterceptor roleCheckHandlerInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.roleCheckHandlerInterceptor = roleCheckHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(roleCheckHandlerInterceptor).addPathPatterns("/admin/**");
    }

}
