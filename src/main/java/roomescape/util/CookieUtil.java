package roomescape.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    private CookieUtil() {
    }

    public static String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public static Cookie createToken(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie clearToken() {
        Cookie cookie = createToken("");
        cookie.setMaxAge(0);
        return cookie;
    }
}
