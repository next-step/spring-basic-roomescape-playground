package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import jwt.JwtService;
import roomescape.member.Member;

import java.util.Arrays;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    public AdminInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractTokenFromCookies(request.getCookies());
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다.");
        }

        Member member = jwtService.getMemberFromToken(token);
        if (!"ADMIN".equals(member.getRole())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다.");
        }

        return true;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
