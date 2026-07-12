package roomescape.login;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    private static final String TOKEN_NAME = "token";

    private CookieUtil() {
    }

    public static String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (TOKEN_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
