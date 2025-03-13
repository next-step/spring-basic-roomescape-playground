package roomescape.auth.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthService;
import roomescape.auth.dto.MemberDetailResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request);
        MemberDetailResponse member = authService.checkLogin(token);

        if (token == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        if (member == null || !member.isAdmin()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
