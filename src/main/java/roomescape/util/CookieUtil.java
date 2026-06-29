package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import roomescape.auth.AuthErrorCode;
import roomescape.exception.ApplicationException;

@Component
public class CookieUtil {

    public void setCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS);
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        throw new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS);
    }
}
