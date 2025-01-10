package roomescape.config;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.jwt.JwtProvider;
import roomescape.member.MemberDao;

import java.util.List;

public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;

    public WebConfig(JwtProvider jwtProvider, MemberDao memberDao) {
        this.jwtProvider = jwtProvider;
        this.memberDao = memberDao;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberDao, jwtProvider));
    }
}
