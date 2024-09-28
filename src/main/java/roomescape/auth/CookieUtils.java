package roomescape.auth;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtils {

    public String getToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("token"))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(RuntimeException::new);
    }

    public Cookie setToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return cookie;
    }

    public Cookie setTokenNull(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return cookie;
    }
}
