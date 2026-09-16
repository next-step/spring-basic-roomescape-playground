package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.CheckAdminInterceptor;
import roomescape.member.LoginMemberArgumentResolver;
import roomescape.member.MemberService;
import roomescape.TokenUtil;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private MemberService memberService;
    private TokenUtil tokenUtil;

    public WebMvcConfiguration(MemberService memberService, TokenUtil tokenUtil) {
        this.memberService = memberService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, tokenUtil));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(memberService, tokenUtil))
                .addPathPatterns("/admin/**");
    }
}
