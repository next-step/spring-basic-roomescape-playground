package roomescape.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.login.LoginService;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final MemberService memberService;

    public AuthorizationInterceptor(LoginService loginService, MemberService memberService) {
        this.loginService = loginService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long memberId = loginService.getMemberId(request.getCookies());
        if (memberId == null) {
            response.setStatus(401);
            return false;
        }

        MemberResponse member = memberService.findById(memberId);
        if (!member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
