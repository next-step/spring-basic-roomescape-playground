package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthService;
import roomescape.auth.MemberDetailResponse;

import java.util.Arrays;

public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request);

        if (token == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        MemberDetailResponse member = authService.loginCheckWithToken(token);
        if (!member.isAdmin()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElse(null);

        return token;
    }
}
