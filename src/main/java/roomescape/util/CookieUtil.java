package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    private static final String TOKEN = "token";

    public void setCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie(TOKEN, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void expireCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public Optional<String> extractToken(Cookie[] cookies) {
        Cookie[] safeCookies = Optional.ofNullable(cookies).orElse(new Cookie[0]);

        return Arrays.stream(safeCookies)
                .filter(cookie -> cookie.getName().equals(TOKEN))
                .map(Cookie::getValue)
                .findFirst();
    }
}
