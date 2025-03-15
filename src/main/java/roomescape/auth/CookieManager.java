package roomescape.auth;

import jakarta.servlet.http.Cookie;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;

import java.util.HashMap;
import java.util.Map;

public class CookieManager {

    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_VALID_TIME = 3600;

    private final Map<String, Cookie> cookieStore = new HashMap<>();

    public CookieManager(Cookie[] cookies) {
        if (cookies == null) {
            throw new BadRequestException(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
        }
        for (Cookie cookie : cookies) {
            cookieStore.put(cookie.getName(), cookie);
        }
    }

    public CookieManager(Cookie cookie) {
        if (cookie == null) {
            throw new BadRequestException(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
        }
        cookieStore.put(cookie.getName(), cookie);
    }

    public static Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_VALID_TIME);
        return cookie;
    }

    public String getValue(String cookieName) {
        Cookie cookie = findCookieByName(cookieName);
        validateCookie(cookie);
        return cookie.getValue();
    }

    private Cookie findCookieByName(String cookieName) {
        if (cookieStore.containsKey(cookieName)) {
            return cookieStore.get(cookieName);
        }
        throw new BadRequestException(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
    }

    private void validateCookie(Cookie cookie) {
        if (cookie.getValue() == null || cookie.getValue().isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_COOKIE_VALUE.getMessage());
        }
    }
}
