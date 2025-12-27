package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.util.CookieUtil;
import roomescape.util.JwtUtil;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    public AdminCheckInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtil.extractToken(request.getCookies());

        if (token == null) {
            response.setStatus(401);
            return false;
        }

        try {
            Long memberId = JwtUtil.getMemberIdFromToken(token);
            Member member = memberService.findById(memberId);

            if (member == null || !"ADMIN".equals(member.getRole())) {
                response.setStatus(401);
                return false;
            }

            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}
