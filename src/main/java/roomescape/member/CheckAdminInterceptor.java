package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.CookieUtil;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final CookieUtil cookieUtil;

    public CheckAdminInterceptor(MemberService memberService, CookieUtil cookieUtil) {
        this.memberService = memberService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = cookieUtil.extractToken(request);
        if (token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        Member member = memberService.findMemberByToken(token);
        if (member.getRole() != Role.ADMIN) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
