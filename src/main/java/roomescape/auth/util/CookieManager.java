package roomescape.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import roomescape.exception.LoginFailedException;

public class CookieManager {

    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_MAX_AGE = 3600;

    private final Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

    public CookieManager(Cookie[] cookies) {

        if (cookies == null || cookies.length == 0) {
            throw new LoginFailedException("사용자 정보를 불러올 수 없습니다.");
        }

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
       for(Cookie cookie : cookieMap.values()) {
           if(cookie.getName().equals(cookieName)) {
               return cookie.getValue();
           }
       }
       return null;
    }
}
