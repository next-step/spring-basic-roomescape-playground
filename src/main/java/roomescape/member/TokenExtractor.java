package roomescape.member;

import jakarta.servlet.http.Cookie;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

import java.util.Arrays;

public class TokenExtractor {

    private static final String COOKIE_NAME = "token";

    private TokenExtractor() {
    }

    public static String extract(Cookie[] cookies) {
        if (cookies == null) {
            throw new BusinessException(ErrorCode.NO_TOKEN);
        }
        return Arrays.stream(cookies)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_TOKEN));
    }
}
