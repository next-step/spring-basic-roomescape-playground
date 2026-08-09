package roomescape.support.querycounter;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class QueryCounterTestConfig {

    @Bean
    public WebMvcConfigurer queryCounterWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new QueryCountInterceptor(new QueryStatementInspector())).addPathPatterns("/**");
            }
        };
    }
}
