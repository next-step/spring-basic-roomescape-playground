package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.UnauthorizedException;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;

    public AdminAuthorizationInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(401);
            return false;
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                Claims claims = jwtUtil.parseToken(token);
                String role = claims.get("role", String.class);
                if (!"ADMIN".equals(role)) {
                    throw new UnauthorizedException("관리자 권한이 필요합니다.");
                }
                return true;
            }
        }

        response.setStatus(401);
        return false;
    }
}
