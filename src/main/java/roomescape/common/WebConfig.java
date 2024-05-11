package roomescape.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private RoleHandlerInterceptor roleHandlerInterceptor;
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(RoleHandlerInterceptor role, LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.roleHandlerInterceptor = role;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleHandlerInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }


}
