package roomescape.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class AuthUtil {
    public static final String TOKEN_NAME = "token";

    protected AuthUtil() {
    }

    public static String extractToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("로그인 토큰이 없습니다. 쿠키를 확인해주세요"));
    }
}
