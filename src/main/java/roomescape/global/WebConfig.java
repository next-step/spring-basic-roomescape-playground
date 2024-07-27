package roomescape.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final RoleHandler roleHandler;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, final RoleHandler roleHandler) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.roleHandler = roleHandler;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleHandler)
                .addPathPatterns("/admin/**");
    }
}
