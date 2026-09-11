package roomescape.member.auth;

import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public final class TokenCookieExtractor {
    private static final String TOKEN_COOKIE_NAME = "token";

    private TokenCookieExtractor() {
    }

    public static String extract(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("로그인 토큰이 없습니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("로그인 토큰이 없습니다."));
    }
}
