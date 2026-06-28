package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {

    public String extract(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        return extractTokenFromCookie(request.getCookies());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
