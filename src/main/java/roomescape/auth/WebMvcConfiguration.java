package roomescape.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginMemberResolver loginMemberResolver;

    public WebMvcConfiguration(LoginMemberResolver loginMemberResolver) {
        this.loginMemberResolver = loginMemberResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(loginMemberResolver));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AdminAuthInterceptor adminAuthInterceptor = new AdminAuthInterceptor(loginMemberResolver);

        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**");

        registry.addInterceptor(new AdminApiInterceptor(adminAuthInterceptor))
                .addPathPatterns("/times", "/times/*", "/themes", "/themes/*", "/reservations/*");
    }
}
