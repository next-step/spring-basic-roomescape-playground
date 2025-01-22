package roomescape.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.authentication.jwt.JwtAuthenticationInfoExtractor;
import roomescape.authentication.jwt.JwtAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    @Bean
    AuthenticationProvider authenticationProvider(){
        return new JwtAuthenticationProvider();
    }

    @Bean
    AuthenticationExtractor authenticationExtractor(){
        return new JwtAuthenticationInfoExtractor();
    }
}
