package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.CheckAdminInterceptor;
import roomescape.member.LoginMemberArgumentResolver;
import roomescape.member.MemberService;
import roomescape.CookieUtil;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private MemberService memberService;
    private CookieUtil cookieUtil;

    public WebMvcConfiguration(MemberService memberService, CookieUtil cookieUtil) {
        this.memberService = memberService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, cookieUtil));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(memberService, cookieUtil))
                .addPathPatterns("/admin/**");
    }
}
