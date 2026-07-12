package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.AuthenticationException;
import roomescape.AuthorizationException;
import roomescape.member.MemberService;
import roomescape.member.MemberRole;

@Component
public class AdminInterceptor implements HandlerInterceptor {
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
            String token = authCookieProvider.extractAccessToken(request);
            LoginMemberInfo loginMember = memberService.checkLogin(token);
            if (MemberRole.ADMIN == loginMember.getRole()) {
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
