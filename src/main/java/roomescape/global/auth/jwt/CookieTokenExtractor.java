package roomescape.global.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.global.excpetion.UnauthorizedException;

import java.util.Arrays;

public class CookieTokenExtractor {

    public static String extract(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            throw new UnauthorizedException();
        }

        Cookie accessTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(UnauthorizedException::new);

        return accessTokenCookie.getValue();
    }
}
