package roomescape.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.AuthService;
import roomescape.application.AuthorizationException;
import roomescape.member.LoginMember;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

@Component
public class AdminAccessInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AdminAccessInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractTokenFromCookies(request.getCookies());

        String email = authService.getEmailFromToken(token);
        LoginMember member = authService.findLoginMemberByEmail(email);

        if (member == null) {
            throw new AuthorizationException("사용자가 존재하지 않습니다..", UNAUTHORIZED);
        }

        if (!"ADMIN".equals(member.getRole())) {
            throw new AuthorizationException("권한이 없습니다.", UNAUTHORIZED);
        }

        return true;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다.", UNAUTHORIZED);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("토큰 쿠키가 없습니다.", UNAUTHORIZED));
    }
}
