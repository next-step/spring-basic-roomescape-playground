package roomescape.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieManager {

    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_MAX_AGE = 3600;

    private final Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

    public CookieManager(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie);
        }
    }

    public static void createCookie(HttpServletResponse response, String cookieName, String cookieValue) {

        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);

    }

    public String getValue(String cookieName) {
        Cookie cookie = cookieMap.get(cookieName);
        if (cookie == null || cookie.getValue().isEmpty()) {
            throw new RuntimeException("Cookie not found: " + cookieName);
        }
        return cookie.getValue();
    }
}
