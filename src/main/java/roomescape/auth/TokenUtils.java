package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import roomescape.exception.AuthorizationException;

public class TokenUtils {
    public static String extractFrom(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
            .filter(cookie -> "token".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
    }

    public static Claims getClaimsFromCookies(Cookie[] cookies, TokenProvider tokenProvider) {
        String token = extractFrom(cookies);

        if (token == null || token.isBlank()) {
            throw new AuthorizationException("인증 토큰이 존재하지 않습니다.");
        }
        return tokenProvider.parseToken(token);
    }
}
