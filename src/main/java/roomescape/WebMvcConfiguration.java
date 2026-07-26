package roomescape;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.JwtUtils;
import roomescape.member.AdminInterceptor;
import roomescape.member.LoginMemberArgumentResolver;
import roomescape.member.MemberRepository;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtUtils jwtUtils;

    public WebMvcConfiguration(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtUtils));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new AdminInterceptor(jwtUtils))
                .addPathPatterns("/admin/**", "/admin");
    }
}
//LoginMemberArgumentResolver을 스프링 MVC에 ㄷ등록해서 사용할 수 있게 해줌
