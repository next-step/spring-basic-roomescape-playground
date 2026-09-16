package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.TokenUtil;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final TokenUtil tokenUtil;

    public CheckAdminInterceptor(MemberService memberService, TokenUtil tokenUtil) {
        this.memberService = memberService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenUtil.extractToken(request);
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
