package roomescape.auth;

import jakarta.servlet.http.Cookie;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;

import java.util.HashMap;
import java.util.Map;

public class CookieManager {

    private final Map<String, String> cookieStore = new HashMap<>();

    public CookieManager(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookieStore.put(cookie.getName(), cookie.getValue());
        }
    }

    public String getValue(String cookieName) {
        if (cookieStore.containsKey(cookieName)) {
            return cookieStore.get(cookieName);
        }
        throw new BadRequestException(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
    }
}
