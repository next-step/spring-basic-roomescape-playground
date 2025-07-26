package roomescape.auth.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.util.JwtUtil;
import roomescape.member.MemberDao;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberDao memberDao;
    private final JwtUtil jwtUtil;

    public WebConfig(MemberDao memberDao, JwtUtil jwtUtil) {
        this.memberDao = memberDao;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberDao, jwtUtil));
    }
}
