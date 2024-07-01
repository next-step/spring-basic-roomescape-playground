package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.infrastructure.JwtTokenUtil;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AuthAdminInteceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthAdminInteceptor(MemberService memberService, JwtTokenUtil jwtTokenUtil) {
        this.memberService = memberService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("--------- : " + request.getCookies());
        Long memberId = jwtTokenUtil.getPayload(request.getCookies());
        Member member = memberService.findMemberById(memberId);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
