package roomescape.domain.auth.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.web.support.annotation.AdminOnly;
import roomescape.domain.auth.web.support.annotation.LoginRequired;
import roomescape.domain.auth.web.support.annotation.Public;
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

        if (!(handlerMethod.getBeanType().getPackageName().startsWith("roomescape"))) {
            return true;
        }

        if (handlerMethod.hasMethodAnnotation(Public.class)) {
            return true;
        }

        boolean adminOnly = handlerMethod.hasMethodAnnotation(AdminOnly.class);
        boolean loginRequired = handlerMethod.hasMethodAnnotation(LoginRequired.class);

        if (!adminOnly && !loginRequired) {
            throw new IllegalArgumentException("API 공개 혹은 인가 정책을 설정하지 않았습니다." + handlerMethod);
        }

        LoginMember loginMember = sessionManager.extractOrNull(request);

        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        if (adminOnly && !loginMember.isAdmin()) {
            throw new ForbiddenException();
        }

        return true;
    }
}
