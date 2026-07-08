package roomescape.auth.web;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.exception.ApplicationException;
import roomescape.util.CookieUtil;

@Component
public class TokenExtractor {

    private final CookieUtil cookieUtil;

    public TokenExtractor(CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
    }

    public String extractToken(Cookie[] cookies) {
        return cookieUtil.extractToken(cookies)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS));
    }
}
