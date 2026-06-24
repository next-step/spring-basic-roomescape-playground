package roomescape;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.AuthorizedMemberArgumentResolver;

@Configuration
public class RoomescapeWebMvcConfigurer implements WebMvcConfigurer {
    private final AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver;

    public RoomescapeWebMvcConfigurer(AuthorizedMemberArgumentResolver authorizedMemberArgumentResolver) {
        this.authorizedMemberArgumentResolver = authorizedMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizedMemberArgumentResolver);
    }
}
