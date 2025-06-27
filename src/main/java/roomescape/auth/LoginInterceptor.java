package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.LoginMember;

public class LoginInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;

    public LoginInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractTokenFromCookies(request.getCookies());

        if (token == null) {
            return true;
        }

        try {
            Claims claims = jwtUtil.parseToken(token);
            LoginMember loginMember = new LoginMember(
                    Long.valueOf(claims.getSubject()),
                    claims.get("name", String.class),
                    null,
                    claims.get("role", String.class)
            );
            request.setAttribute("loginMember", loginMember);
        } catch (Exception e) {
        }

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
