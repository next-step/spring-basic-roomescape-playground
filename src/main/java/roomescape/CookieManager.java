package roomescape;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    public static final String COOKIE_NAME = "token";
    public static final String ROOT_URI = "/";

    public Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookies = Objects.requireNonNull(request).getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findAny()
                .orElseThrow(() -> new RuntimeException("토큰이 존재하지 않습니다."));
    }

    public void setCookie(String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setPath(ROOT_URI);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

}
