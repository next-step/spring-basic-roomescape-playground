package roomescape.auth.session.cookie;

import java.time.Duration;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import roomescape.auth.session.jwt.JwtProperties;

@Component
public class CookieProvider {

    public static final String PATH = "/";

    public ResponseCookie generateCookie(String value, Duration duration) {
        ResponseCookie responseCookie = ResponseCookie.from(JwtProperties.TOKEN, value)
                .path(PATH)
                .httpOnly(true)
                .maxAge(duration)
                .build();
        return responseCookie;
    }
}
