package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.auth.TokenProvider;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenProvider tokenProvider;

    public WebConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(tokenProvider));
    }
}
