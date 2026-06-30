package roomescape;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminHandlerInterceptor;
import roomescape.auth.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminHandlerInterceptor adminHandlerInterceptor;

    public WebMvcConfiguration(LoginMemberArgumentResolver loginMemberArgumentResolver, AdminHandlerInterceptor adminHandlerInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminHandlerInterceptor = adminHandlerInterceptor;
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminHandlerInterceptor).addPathPatterns("/admin/**");
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }
}