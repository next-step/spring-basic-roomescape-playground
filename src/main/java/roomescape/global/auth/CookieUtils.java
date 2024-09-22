package roomescape.global.auth;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static void setTokenCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("token"))
            .map(Cookie::getValue)
            .findFirst()
            .orElse("");
    }
}
