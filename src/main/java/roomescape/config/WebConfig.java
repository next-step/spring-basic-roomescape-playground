package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminInterceptor;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.jwt.JwtProvider;
import roomescape.member.MemberDao;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final MemberDao memberDao;

    public WebConfig(JwtProvider jwtProvider, MemberDao memberDao) {
        this.jwtProvider = jwtProvider;
        this.memberDao = memberDao;
    }

    @Override //3단계
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberDao, jwtProvider));
    }

    @Override //3단계
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtProvider, memberDao))
            .addPathPatterns("/admin/**");
    }
}
