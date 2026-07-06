package roomescape.auth.config.utils;

import jakarta.servlet.http.Cookie;

public class CookieProvider {
    private static final int MAX_AGE = 60 * 60 * 24; // 쿠키 유효기간 1일
    private static final String COOKIE_NAME = "token";

    public static Cookie provideCookie(String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(MAX_AGE);
        cookie.setPath("/");

        return cookie;
    }
}
