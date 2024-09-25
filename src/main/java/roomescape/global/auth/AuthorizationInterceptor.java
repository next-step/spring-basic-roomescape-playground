package roomescape.global.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final CookieUtil cookieUtil;

    @Autowired
    public AuthorizationInterceptor(MemberService memberService, CookieUtil cookieUtil) {
        this.memberService = memberService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = cookieUtil.extractTokenFromCookie(cookies);

        if(token.isEmpty()) return false;

        Member member = memberService.getAuth(token);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
