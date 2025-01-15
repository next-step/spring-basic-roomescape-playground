package auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.AuthorizationException;

import java.util.Arrays;

@Component
public class AdminAccessInterceptor implements HandlerInterceptor {
    private final JwtAuthManager jwtAuthManager;

    public AdminAccessInterceptor(JwtAuthManager jwtAuthManager) {
        this.jwtAuthManager = jwtAuthManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractTokenFromCookies(request.getCookies());
        jwtAuthManager.validateToken(token);

        String role = jwtAuthManager.getRole(token);

        if (!"ADMIN".equals(role)) {
            response.setStatus(401);
            response.getWriter().write("권한이 없습니다.");
            return false;
        }

        return true;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("토큰 쿠키가 없습니다."));
    }
}
