package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AdminPageInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private AdminPageInterceptor adminPageInterceptor;

    public InterceptorConfig(AdminPageInterceptor adminPageInterceptor) {
        this.adminPageInterceptor = adminPageInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminPageInterceptor)
                .addPathPatterns("/admin");
    }
}
