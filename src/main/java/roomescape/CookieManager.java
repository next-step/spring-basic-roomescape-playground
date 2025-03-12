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

    // TODO 그냥 토큰 값을 반환 ?
    public Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookies = Objects.requireNonNull(request).getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findAny()
                .orElseThrow(() -> new RuntimeException("쿠키가 존재하지 않습니다."));
    }

    // TODO 토큰을 set ? 의미를 나타내지 못함! 무엇을 set 하는지 값들을 구분할 수 없을까?
    public void setCookie(String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setPath(ROOT_URI);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

}
