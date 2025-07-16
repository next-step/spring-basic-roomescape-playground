package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.UnauthorizedException;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    public LoginInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractTokenFromCookies(request.getCookies());

        if (token == null) {
            if (request.getRequestURI().startsWith("/reservations")) {
                throw new UnauthorizedException("로그인이 필요합니다.");
            }
            return true;
        }

        Claims claims = jwtUtils.parseToken(token);
        LoginMember loginMember = new LoginMember(
                Long.valueOf(claims.getSubject()),
                claims.get("name", String.class),
                null,
                claims.get("role", String.class)
        );
        request.setAttribute("loginMember", loginMember);

        return true;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
