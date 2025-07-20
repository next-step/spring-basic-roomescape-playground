package roomescape.global.util;

import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class CookieUtil {
    private CookieUtil() {}

    public static Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return "";

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}
