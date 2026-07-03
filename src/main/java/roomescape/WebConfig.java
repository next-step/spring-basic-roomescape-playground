package roomescape;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.LoginMemberArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
        // 로그인에 이 resolver를 사용해라
    }
}

/*

컨트롤러 메서드 파라미터를 해석할 떄, LoginMemberArguementResolver 도사용함
@LoginMember 파라미터가 있을 때 resolver를 사용하기 위해서임
스프링 MVC에 등록 - 스프링이 알아서 해주는거임


 */