package roomescape;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.admin.AdminInterceptor;
import roomescape.login.LoginMemberArgumentResolver;
import roomescape.member.MemberService;
import roomescape.util.JwtUtil;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public WebConfig(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtUtil))
                .addPathPatterns("/admin/**");
    }
}
