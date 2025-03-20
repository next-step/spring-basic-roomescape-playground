package roomescape.common.cookie;

import jakarta.servlet.http.Cookie;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.exception.UnAuthorizedException;

import java.util.Arrays;
import java.util.Optional;

public class CookieManager {

    public static final String AUTH_TOKEN_COOKIE = "token";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_VALID_TIME = 3600;

    private CookieManager() {
    }

    public static Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_VALID_TIME);
        return cookie;
    }

    public static String getToken(Cookie[] cookies) {
        Cookie cookie = findCookieByName(cookies, AUTH_TOKEN_COOKIE)
                .orElseThrow(() -> new UnAuthorizedException(ExceptionMessage.AUTHENTICATION_NEEDED.getMessage()));
        validateCookie(cookie);
        return cookie.getValue();
    }

    private static Optional<Cookie> findCookieByName(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findAny();
    }

    private static void validateCookie(Cookie cookie) {
        if (cookie.getValue() == null || cookie.getValue().isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_COOKIE_VALUE.getMessage());
        }
    }
}
