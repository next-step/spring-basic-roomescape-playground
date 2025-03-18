package roomescape.auth.client.cookie;

import java.time.Duration;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public ResponseCookie generateCookie(String value, Duration duration) {
        return ResponseCookie.from(CookieProperties.TOKEN, value)
                .path(CookieProperties.PATH)
                .httpOnly(true)
                .maxAge(duration)
                .build();
    }
}
