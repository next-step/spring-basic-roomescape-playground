package roomescape.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    private CookieUtil() {}

    public static Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
