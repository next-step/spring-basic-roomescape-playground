package roomescape.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
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
        return loginService.getMemberId(request.getCookies())
            .map(memberId -> {
                MemberResponse member = memberService.findById(memberId);
                if (!member.isAdmin()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
                return true;
            })
            .orElseGet(() -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            });
    }
}
