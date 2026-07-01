package roomescape;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminInterceptor;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.auth.TokenExtractor;
import roomescape.member.MemberService;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberService memberService;
    private final TokenExtractor tokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(
            MemberService memberService,
            TokenExtractor tokenExtractor,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberService = memberService;
        this.tokenExtractor = tokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, tokenExtractor, jwtTokenProvider));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(memberService, tokenExtractor, jwtTokenProvider))
                .addPathPatterns("/admin", "/admin/**");
    }
}
