package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.golbal.AuthHandlerInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private AuthHandlerInterceptor authHandlerInterceptor;

    public WebConfig(AuthHandlerInterceptor authHandlerInterceptor) {
        this.authHandlerInterceptor = authHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor)
            .addPathPatterns("/admin/**");
    }
}
