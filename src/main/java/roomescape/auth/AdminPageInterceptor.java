package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.api.JwtDecoder;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminPageInterceptor implements HandlerInterceptor {
    private MemberService memberService;

    public AdminPageInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = JwtDecoder.extractTokenFromCookie(request.getCookies());
        Long id = JwtDecoder.decodeJwtToken(token);
        Member member = memberService.findById(id);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
