package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieTokenExtractor {
    public String extract(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null && !token.isBlank()) {
                        return token;
                    }
                    break;
                }
            }
        }
        throw new InvalidTokenException("로그인 토큰이 없습니다.");
    }
}
