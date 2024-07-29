package roomescape.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.MemberService;
import roomescape.interceptor.AdminInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final String secretKey;

    public WebConfig(MemberService memberService, @Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.memberService = memberService;
        this.secretKey = secretKey;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(memberService, secretKey))
                .addPathPatterns("/admin/**");
    }
}
