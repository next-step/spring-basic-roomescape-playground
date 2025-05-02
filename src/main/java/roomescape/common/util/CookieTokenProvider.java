package roomescape.common.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieTokenProvider {

    public Optional<String> extractToken(Cookie[] cookies) {
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(c -> "token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
