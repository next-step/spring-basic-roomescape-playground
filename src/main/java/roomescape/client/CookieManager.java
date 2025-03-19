package roomescape.client;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;

import java.util.Arrays;

public class CookieManager {

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

    public static String getValue(Cookie[] cookies, String cookieName) {
        Cookie cookie = findCookieByName(cookies, cookieName);
        validateCookie(cookie);
        return cookie.getValue();
    }

    private static Cookie findCookieByName(Cookie[] cookies, String cookieName) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.COOKIE_NOT_FOUND.getMessage()));
    }

    private static void validateCookie(Cookie cookie) {
        if (cookie.getValue() == null || cookie.getValue().isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_COOKIE_VALUE.getMessage());
        }
    }
}
