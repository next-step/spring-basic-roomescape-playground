package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod) || !isAdminOnly(handlerMethod)) {
            return true;
        }

        try {
            String token = TokenCookieExtractor.extract(request.getCookies());
            LoginMember loginMember = memberService.findLoginMemberByToken(token);

            if (!loginMember.isAdmin()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            return true;
        } catch (IllegalArgumentException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private boolean isAdminOnly(HandlerMethod handlerMethod) {
        return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), AdminOnly.class)
                || AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), AdminOnly.class);
    }
}
