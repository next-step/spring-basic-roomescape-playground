package roomescape.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    public static String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
