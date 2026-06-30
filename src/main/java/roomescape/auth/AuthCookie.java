package roomescape.auth;

import jakarta.servlet.http.Cookie;

public final class AuthCookie {
    public static final String TOKEN_NAME = "token";

    public static Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie createExpiredTokenCookie() {
        Cookie cookie = createTokenCookie("");
        cookie.setMaxAge(0);
        return cookie;
    }

    private AuthCookie() {
    }
}
