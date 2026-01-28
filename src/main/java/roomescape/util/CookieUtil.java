package roomescape.util;

import jakarta.servlet.http.Cookie;

public final class CookieUtil {
    private CookieUtil() {
    }

    public static Cookie createHttpOnlyCookie(String name, String value, int maxAgeSeconds, boolean secure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setSecure(secure);
        return cookie;
    }

    public static Cookie expireCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}


