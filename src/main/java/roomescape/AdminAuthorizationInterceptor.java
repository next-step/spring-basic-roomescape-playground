package roomescape;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

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
                try {
                    Claims claims = jwtUtil.parseToken(token);
                    String role = claims.get("role", String.class);
                    if ("ADMIN".equals(role)) {
                        return true;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }

        response.setStatus(401);
        return false;
    }
}
