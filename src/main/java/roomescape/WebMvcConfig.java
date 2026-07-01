package roomescape;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import roomescape.member.AdminInterceptor;
import roomescape.member.LoginMemberArgumentResolver;
import roomescape.member.MemberService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final AdminInterceptor adminInterceptor;

    public WebMvcConfig(MemberService memberService, AdminInterceptor adminInterceptor) {
        this.memberService = memberService;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, memberService.getSecretKey()));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin", "/admin/**", "/reservations", "/reservations/**");
    }
}
