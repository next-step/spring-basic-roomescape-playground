package roomescape;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.exception.TokenNotFoundException;

import java.util.Arrays;

@Component
public class CookieManager {
    public String extractToken(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(TokenNotFoundException::new);
    }
}
