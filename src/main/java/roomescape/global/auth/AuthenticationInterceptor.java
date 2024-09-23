package roomescape.global.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public AuthenticationInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        Cookie[] cookies = request.getCookies();
        String token = CookieUtils.extractTokenFromCookie(cookies != null ? cookies : new Cookie[0]);

        if (token.isEmpty())    return false;

        Member member = memberService.checkLogin(token);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
