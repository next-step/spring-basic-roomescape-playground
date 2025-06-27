package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.exception.TokenNotFoundException;

@Component
public class CookieValueExtractor {

    public String extractToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new TokenNotFoundException();
    }

}
