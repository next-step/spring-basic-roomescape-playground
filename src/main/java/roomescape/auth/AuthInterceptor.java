package roomescape.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthService;
import roomescape.auth.LoginResponse;

import java.util.Arrays;

public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("/login");
            return false;
        }

        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (tokenCookie == null) {
            response.sendRedirect("/login");
            return false;
        }

        LoginResponse loginResponse = authService.findUserByToken(tokenCookie.getValue());

        if (loginResponse == null) {
            response.sendRedirect("/login");
            return false;
        }

        if ("ADMIN".equals(loginResponse.getRole())) {
            return true; // 관리자 페이지 접근 허용
        } else {
            response.sendRedirect("/access-denied");
            return false;
        }
    }
}
