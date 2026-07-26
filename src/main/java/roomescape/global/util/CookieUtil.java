package roomescape.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String ACCESS_TOKEN_PATH = "/";
    private static final String REFRESH_TOKEN_PATH = "/login/refresh";


    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        response.addCookie(
                createCookie(ACCESS_TOKEN, accessToken, ACCESS_TOKEN_PATH)
        );
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        response.addCookie(
                createCookie(REFRESH_TOKEN, refreshToken, REFRESH_TOKEN_PATH)
        );
    }

    public void expireAccessTokenCookie(HttpServletResponse response) {
        response.addCookie(expireCookie(ACCESS_TOKEN, ACCESS_TOKEN_PATH));
    }

    public void expireRefreshTokenCookie(HttpServletResponse response) {
        response.addCookie(expireCookie(REFRESH_TOKEN, REFRESH_TOKEN_PATH));
    }

    public Optional<String> extractAccessToken(Cookie[] cookies) {
        return extractToken(cookies, ACCESS_TOKEN);
    }

    public Optional<String> extractRefreshToken(Cookie[] cookies) {
        return extractToken(cookies, REFRESH_TOKEN);
    }

    private Cookie createCookie(String name, String value, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath(path);
        return cookie;
    }

    private Cookie expireCookie(String name, String path) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setPath(path);
        cookie.setMaxAge(0);

        return cookie;
    }

    private Optional<String> extractToken(Cookie[] cookies, String name) {
        Cookie[] safeCookies = Optional.ofNullable(cookies).orElse(new Cookie[0]);

        return Arrays.stream(safeCookies)
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst();
    }
}
