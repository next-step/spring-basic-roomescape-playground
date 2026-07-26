package roomescape.auth.web;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.global.exception.ApplicationException;
import roomescape.global.util.CookieUtil;

@Component
public class TokenExtractor {

    private final CookieUtil cookieUtil;

    public TokenExtractor(CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
    }

    public String extractAccessToken(Cookie[] cookies) {
        return cookieUtil.extractAccessToken(cookies)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS));
    }

    public String extractRefreshToken(Cookie[] cookies) {
        return cookieUtil.extractRefreshToken(cookies)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS));
    }
}
