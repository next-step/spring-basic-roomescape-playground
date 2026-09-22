package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    private MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = memberService.extractTokenFromCookie(cookies);
        Member member = memberService.tokenToMember(token);
        if (member.getRole().equals("ADMIN")) {
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
