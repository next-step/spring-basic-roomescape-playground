package auth;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.member.MemberRepository;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    private final JwtProperties properties;

    public JwtConfig(JwtProperties props) {
        this.properties = props;
    }

    @Bean
    public JwtService jwtUtil(MemberRepository memberRepo) {
        return new JwtService(properties.getSecret(), memberRepo);
    }
}
