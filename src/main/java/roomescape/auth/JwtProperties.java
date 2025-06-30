package roomescape.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "roomescape.auth.jwt")
public class JwtProperties {
    private final String secret;
    private final Long expirationMs;

    public JwtProperties(String secret, Long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String getSecret() {
        return secret;
    }

    public Long getExpirationMs() {
        return expirationMs;
    }
}
