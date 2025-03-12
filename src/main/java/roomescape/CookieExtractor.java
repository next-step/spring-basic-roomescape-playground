package roomescape;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class CookieExtractor {

    public static final String COOKIE_NAME = "token";

    public Cookie extractToken(HttpServletRequest request) {
        Cookie[] cookies = Objects.requireNonNull(request).getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findAny()
                .orElseThrow(() -> new RuntimeException("토큰이 존재하지 않습니다."));
    }

}
