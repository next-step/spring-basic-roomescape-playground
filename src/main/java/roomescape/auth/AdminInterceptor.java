package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.AuthenticationException;
import roomescape.AuthorizationException;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private static final String ADMIN_ROLE = "ADMIN";

    private final AuthCookieProvider authCookieProvider;
    private final MemberService memberService;

    public AdminInterceptor(AuthCookieProvider authCookieProvider, MemberService memberService) {
        this.authCookieProvider = authCookieProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod) || !hasAdminOnly(handlerMethod)) {
            return true;
        }

        try {
            String token = authCookieProvider.extractToken(request);
            LoginMemberInfo loginMember = memberService.checkLogin(token);
            if (ADMIN_ROLE.equals(loginMember.getRole())) {
                return true;
            }
            throw new AuthorizationException();
        } catch (AuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new AuthenticationException();
        }
    }

    private boolean hasAdminOnly(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(AdminOnly.class)
                || handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);
    }
}
