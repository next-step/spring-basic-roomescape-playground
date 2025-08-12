package roomescape.auth.configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.util.JwtUtil;

public class AdminHandlerInterceptor implements HandlerInterceptor {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String TOKEN_COOKIE_NAME = "token";

    private final JwtUtil jwtUtil;

    public AdminHandlerInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        String token = extractToken(request);
        if (token == null) {
            sendUnauthorizedError(response);
            return false;
        }

        if (!isAdmin(token)) {
            sendUnauthorizedError(response);
            return false;
        }

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return findTokenFromCookies(cookies);
    }

    private String findTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isAdmin(String token) {
        String role = jwtUtil.parseToken(token).role();
        return ADMIN_ROLE.equals(role);
    }

    private void sendUnauthorizedError(HttpServletResponse response) throws Exception {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
