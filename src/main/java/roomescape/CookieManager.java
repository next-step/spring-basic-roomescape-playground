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

    public String getTokenFrom(HttpServletRequest request) {
        Cookie[] cookies = Objects.requireNonNull(request).getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findAny()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("로그인 필요"));
    }

    public void addTokenToCookie(String token, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setPath(ROOT_URI);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

}
