package roomescape.domain.auth.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.global.exception.ForbiddenException;
import roomescape.global.exception.UnauthorizedException;

public class RoleInterceptor implements HandlerInterceptor {

    private final String requiredRole;
    private final SessionManager sessionManager;

    public RoleInterceptor(String requiredRole, SessionManager sessionManager) {
        this.requiredRole = requiredRole;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginMember loginMember = sessionManager.extractOrNull(request);

        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        if (!requiredRole.equals(loginMember.getRole())) {
            throw new ForbiddenException();
        }

        return true;
    }
}
