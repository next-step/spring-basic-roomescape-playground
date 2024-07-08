package roomescape.global;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import roomescape.global.constant.AuthConstant;

public class CookieUtil {

    public static String getTokenFromCookie(final HttpServletRequest request,
                                            final AuthConstant key) {
        final Cookie[] cookies = request.getCookies();
        String token = null;
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(key.getValue()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
    }
}
