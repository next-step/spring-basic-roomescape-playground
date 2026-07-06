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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod) || !hasAdminOnly(handlerMethod)) {
            return true;
        }

        LoginMemberInfo member = findMember(request);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private LoginMemberInfo findMember(HttpServletRequest request) {
        try {
            String token = authCookieProvider.extractToken(request);
            return memberService.checkLogin(token);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private boolean hasAdminOnly(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(AdminOnly.class)
                || handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);
    }
}
