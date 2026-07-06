package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.MemberService;

@Component
public class AdminOnlyInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final AuthCookieProvider authCookieProvider;

    public AdminOnlyInterceptor(MemberService memberService, AuthCookieProvider authCookieProvider) {
        this.memberService = memberService;
        this.authCookieProvider = authCookieProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod) || !hasAdminOnly(handlerMethod)) {
            return true;
        }

        try {
            String token = authCookieProvider.extractToken(request);
            LoginMemberInfo loginMember = memberService.checkLogin(token);
            if (loginMember.isAdmin()) {
                return true;
            }
        } catch (RuntimeException e) {
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    private boolean hasAdminOnly(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(AdminOnly.class)
                || handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);
    }
}
