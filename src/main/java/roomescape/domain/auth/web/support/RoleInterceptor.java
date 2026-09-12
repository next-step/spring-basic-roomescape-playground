package roomescape.domain.auth.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.global.exception.ForbiddenException;
import roomescape.global.exception.UnauthorizedException;

public class RoleInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;

    public RoleInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        if (!(handlerMethod.hasMethodAnnotation(AdminOnly.class))) {
            return true;
        }

        LoginMember loginMember = sessionManager.extractOrNull(request);

        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        if (!loginMember.isAdmin()) {
            throw new ForbiddenException();
        }

        return true;
    }
}
