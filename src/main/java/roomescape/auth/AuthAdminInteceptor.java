package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AuthAdminInteceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    public AuthAdminInteceptor(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("--------- : " + request.getCookies());
        Long memberId = jwtUtils.getPayload(request.getCookies());
        Member member = memberService.findMemberById(memberId);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
