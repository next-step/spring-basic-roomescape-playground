package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.AuthenticationException;

@Component
public class AuthCookieProvider {
    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final String LEGACY_TOKEN_COOKIE_NAME = "token";
    private static final String COOKIE_PATH = "/";

    public Cookie createLoginCookie(String token) {
        return createAccessTokenCookie(token);
    }

    public Cookie createAccessTokenCookie(String token) {
        return createCookie(ACCESS_TOKEN_COOKIE_NAME, token);
    }

    public Cookie createRefreshTokenCookie(String token) {
        return createCookie(REFRESH_TOKEN_COOKIE_NAME, token);
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        return cookie;
    }

    public Cookie createLogoutCookie() {
        return createLogoutCookie(LEGACY_TOKEN_COOKIE_NAME);
    }

    public Cookie createLogoutAccessTokenCookie() {
        return createLogoutCookie(ACCESS_TOKEN_COOKIE_NAME);
    }

    public Cookie createLogoutRefreshTokenCookie() {
        return createLogoutCookie(REFRESH_TOKEN_COOKIE_NAME);
    }

    private Cookie createLogoutCookie(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        return cookie;
    }

    public String extractToken(HttpServletRequest request) {
        return extractAccessToken(request);
    }

    public String extractAccessToken(HttpServletRequest request) {
        return extractToken(request, ACCESS_TOKEN_COOKIE_NAME, LEGACY_TOKEN_COOKIE_NAME);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return extractToken(request, REFRESH_TOKEN_COOKIE_NAME);
    }

    private String extractToken(HttpServletRequest request, String... cookieNames) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationException();
        }

        for (Cookie cookie : cookies) {
            if (contains(cookieNames, cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new AuthenticationException();
    }

    private boolean contains(String[] values, String target) {
        for (String value : values) {
            if (value.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
