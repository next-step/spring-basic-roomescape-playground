package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.AuthorizationException;
import roomescape.member.MemberRole;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final AuthCookieProvider authCookieProvider;
    private final AuthTokenService authTokenService;

    public AdminInterceptor(AuthCookieProvider authCookieProvider, AuthTokenService authTokenService) {
        this.authCookieProvider = authCookieProvider;
        this.authTokenService = authTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod) || !hasAdminOnly(handlerMethod)) {
            return true;
        }

        String token = authCookieProvider.extractAccessToken(request);
        LoginMemberInfo loginMember = authTokenService.parseAccessToken(token);
        if (MemberRole.ADMIN == loginMember.role()) {
            return true;
        }
        throw new AuthorizationException();
    }

    private boolean hasAdminOnly(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(AdminOnly.class)
                || handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);
    }
}
