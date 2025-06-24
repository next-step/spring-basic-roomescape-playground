package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.member.MemberService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public WebConfig(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider));
    }
}
