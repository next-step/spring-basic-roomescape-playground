package roomescape.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import roomescape.error.ErrorMessage;

public class AuthUtil {
    public static final String TOKEN_NAME = "token";

    private AuthUtil() {
    }

    public static String extractToken(HttpServletRequest request) {
        if (Objects.isNull(request.getCookies())) {
            throw new IllegalArgumentException(ErrorMessage.NO_COOKIES_FOUND.getMessage());
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NO_AUTH_TOKEN_FOUND.getMessage()));
    }
}
