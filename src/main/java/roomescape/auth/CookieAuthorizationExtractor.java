package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class CookieAuthorizationExtractor implements AuthorizationExtractor {
    private static final String AUTHORIZATION_NAME = "token";

    @Override
    public String extract(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(AUTHORIZATION_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Empty cookie"));
    }
}
