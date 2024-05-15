package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.AdminHandlerInterceptor;
import roomescape.member.LoginMemberArgumentResolver;
import roomescape.member.MemberService;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private LoginMemberArgumentResolver loginMemberArgumentResolver;
    private MemberService memberService;

    public WebMvcConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, MemberService memberService) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminHandlerInterceptor(memberService))
                .addPathPatterns("/admin/**");
    }
}
