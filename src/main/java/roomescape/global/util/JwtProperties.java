package roomescape.global.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt.token")
public class JwtProperties {
    private String secretKey;
    private long expireLength;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getExpireLength() {
        return expireLength;
    }

    public void setExpireLength(long expireLength) {
        this.expireLength = expireLength;
    }
}