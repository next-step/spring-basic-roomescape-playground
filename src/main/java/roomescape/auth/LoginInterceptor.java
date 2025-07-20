package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;

public class LoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
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

        Member member = authService.checkLogin(token);
        LoginMember loginMember = new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
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
