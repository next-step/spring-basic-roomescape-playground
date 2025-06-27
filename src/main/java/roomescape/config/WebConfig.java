package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.CheckAdminInterceptor;
import roomescape.auth.CookieValueExtractor;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.member.MemberService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final CookieValueExtractor cookieValueExtractor;

    public WebConfig(JwtTokenProvider jwtTokenProvider, MemberService memberService,
                     CookieValueExtractor cookieValueExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.cookieValueExtractor = cookieValueExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider, cookieValueExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminInterceptor(memberService, jwtTokenProvider, cookieValueExtractor))
                .addPathPatterns("/admin/**");
    }
}
