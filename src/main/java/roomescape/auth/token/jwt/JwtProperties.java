package roomescape.auth.token.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtProperties {
    public static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    public static final String NAME = "name";
    public static final String ROLE = "role";
    public static final String TOKEN = "token";
    public static final String ADMIN = "ADMIN";
    public static final String EXPIRED_TOKEN = "";
    public static final Long DEFAULT_TIME = 60L;
}
