package roomescape.global;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Component
public class RoleHandler implements HandlerInterceptor {

    private final MemberService memberService;

    public RoleHandler(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookie =request.getCookies();
        String token = memberService.extractTokenFromCookie(cookie);
        MemberResponse member = memberService.findByToken(token);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}