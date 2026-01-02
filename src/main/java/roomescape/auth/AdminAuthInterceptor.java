package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.util.JwtUtil;

public class AdminAuthInterceptor implements HandlerInterceptor {

    private final String secretKey;

    public AdminAuthInterceptor(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = JwtUtil.extractTokenFromCookies(request.getCookies());
        if (token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        try {
            Claims claims = JwtUtil.parseClaims(token, secretKey);
            String role = claims.get("role", String.class);
            if (!"ADMIN".equals(role)) {
                response.setStatus(401);
                return false;
            }
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}


