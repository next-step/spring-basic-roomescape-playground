package roomescape.config;

import auth.JwtAuthManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import auth.AdminAccessInterceptor;
import auth.LoginMemberArgumentResolver;
import roomescape.domain.member.MemberRepository;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"roomescape", "auth"})
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtAuthManager jwtAuthManager;
    private final MemberRepository memberRepository;

    public WebMvcConfig(JwtAuthManager jwtAuthManager, MemberRepository memberRepository) {
        this.jwtAuthManager = jwtAuthManager;
        this.memberRepository = memberRepository;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtAuthManager, memberRepository));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAccessInterceptor(jwtAuthManager))
                .addPathPatterns("/admin/**");
    }
}
