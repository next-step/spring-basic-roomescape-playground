package roomescape.auth;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
