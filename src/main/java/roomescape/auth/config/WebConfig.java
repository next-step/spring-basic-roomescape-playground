package roomescape.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.MemberService;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    public WebConfig(TokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(tokenProvider, memberService));
    }
}