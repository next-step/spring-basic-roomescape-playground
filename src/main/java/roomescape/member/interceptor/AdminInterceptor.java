package roomescape.member.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;
import roomescape.member.annotation.AdminOnly;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (!requiresAdmin(handler)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            throw new MemberException(MemberErrorCode.LOGIN_REQUIRED);
        }

        Member member = memberService.getMember(memberId);
        if (!"ADMIN".equals(member.getRole())) {
            throw new MemberException(MemberErrorCode.ADMIN_REQUIRED);
        }

        return true;
    }

    private boolean requiresAdmin(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }

        return handlerMethod.hasMethodAnnotation(AdminOnly.class);
    }
}
