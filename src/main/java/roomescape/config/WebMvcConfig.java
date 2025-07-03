package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.login.LoginMemberArgumentResolver;
import roomescape.member.MemberDao;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberDao memberDao;
    private final String secretKey = System.getenv()
            .getOrDefault("JWT_SECRET_KEY",
                    "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=");

    public WebMvcConfig(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(secretKey, memberDao));
    }
}
