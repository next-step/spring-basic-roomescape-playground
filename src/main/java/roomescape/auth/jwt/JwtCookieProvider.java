package roomescape.auth.jwt;

import org.springframework.http.ResponseCookie;

public final class JwtCookieProvider {
    static final String TOKEN_COOKIE_NAME = "token";

    private JwtCookieProvider() {
    }

    public static ResponseCookie loginCookie(String token) {
        return ResponseCookie.from(TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .path("/")
                .build();
    }

    public static ResponseCookie logoutCookie() {
        return ResponseCookie.from(TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
    }
}
