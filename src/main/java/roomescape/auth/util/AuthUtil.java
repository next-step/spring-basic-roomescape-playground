package roomescape.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

public class AuthUtil {
    public static final String TOKEN_NAME = "token";

    private AuthUtil() {
    }

    public static String extractToken(HttpServletRequest request) {
        if (Objects.isNull(request.getCookies())) {
            throw new IllegalArgumentException("쿠키가 없습니다. 로그인 상태를 확인해주세요.");
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("로그인 토큰이 없습니다. 쿠키를 확인해주세요"));
    }
}
