package roomescape;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

public class AdminInterceptor implements HandlerInterceptor {
    private MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Member member = memberService.getMemberFromCookie(request);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
        }
        return true;
    }
}
