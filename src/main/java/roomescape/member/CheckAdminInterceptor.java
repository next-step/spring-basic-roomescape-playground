package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.AuthService;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Member member = authService.findAuthenticatedMember(request);
        if (member.getRole() != Role.ADMIN) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
