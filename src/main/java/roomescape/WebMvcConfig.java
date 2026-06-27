package roomescape;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.login.LoginMemberArgumentResolver;
import roomescape.member.MemberDao;

import java.util.List;
import roomescape.token.TokenProvider;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public WebMvcConfig(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberDao));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(tokenProvider))
                .addPathPatterns("/admin/**");
    }
}
