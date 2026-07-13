package roomescape.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("roomescape.auth.jwt")
public record JwtTokenProperty(
        String secretKey,
        long accessTokenExpiration,
        long refreshTokenExpiration
) {
}
