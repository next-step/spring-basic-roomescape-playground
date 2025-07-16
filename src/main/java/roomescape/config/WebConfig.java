package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.CheckAdminInterceptor;
import roomescape.auth.CookieValueExtractor;
import auth.JwtUtils;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.member.MemberService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtUtils jwtUtils;
    private final MemberService memberService;
    private final CookieValueExtractor cookieValueExtractor;

    public WebConfig(JwtUtils jwtUtils, MemberService memberService,
                     CookieValueExtractor cookieValueExtractor) {
        this.jwtUtils = jwtUtils;
        this.memberService = memberService;
        this.cookieValueExtractor = cookieValueExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtUtils, cookieValueExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(jwtUtils, cookieValueExtractor))
                .addPathPatterns("/admin/**");
    }

}
