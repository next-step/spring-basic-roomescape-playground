package roomescape.auth;

import auth.JwtUtilsV4;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.InvalidRoleException;
import roomescape.exception.InvalidTokenException;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final JwtUtilsV4 jwtUtils;
    private final CookieValueExtractor cookieValueExtractor;

    public CheckAdminInterceptor(JwtUtilsV4 jwtUtils, CookieValueExtractor cookieValueExtractor) {
        this.jwtUtils = jwtUtils;
        this.cookieValueExtractor = cookieValueExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = cookieValueExtractor.extractToken(request.getCookies());
            if (token == null) {
                throw new InvalidTokenException();
            }
            Claims claims = jwtUtils.getClaims(token);
            String role = claims.get("role", String.class);
            if (!"ADMIN".equals(role)) {
                throw new InvalidRoleException();
            }
            return true;
        } catch (JwtException | NullPointerException e) {
            throw new InvalidTokenException();
        }
    }
}
