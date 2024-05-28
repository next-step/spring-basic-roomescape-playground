package roomescape.member;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.JwtUtils;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
    private LoginMemberArgumentResolver loginMemberArgumentResolver;
    private AdminInterceptor adminInterceptor;
    private JwtUtils jwtUtils;


    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver(JwtUtils jwtUtils) {
        return new LoginMemberArgumentResolver(jwtUtils);
    }

    @Bean
    public AdminInterceptor adminInterceptor(JwtUtils jwtUtils) {
        return new AdminInterceptor(jwtUtils);
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    public WebConfiguration(LoginMemberArgumentResolver loginMemberArgumentResolver, AdminInterceptor adminInterceptor) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver(jwtUtils()));
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor(jwtUtils()))
                .addPathPatterns("/admin/**");
    }
}