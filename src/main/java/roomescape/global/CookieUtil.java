package roomescape.global;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.constant.AuthConstant;

public class CookieUtil {

    public static String getTokenFromCookie(final HttpServletRequest request,
                                            final AuthConstant key) {
        final Cookie[] cookies = request.getCookies();
        String token = null;
        for (final Cookie cookie : cookies) {
            final String name = cookie.getName();
            if (name.equals(key.getValue())) {
                token = cookie.getValue();
                break;
            }
        }
        return token;
    }
}
