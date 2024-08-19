package roomescape.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import roomescape.member.MemberService;
import roomescape.member.MemberResponse;

public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final String secretKey;

    public AdminInterceptor(MemberService memberService, String secretKey) {
        this.memberService = memberService;
        this.secretKey = secretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie tokenCookie = WebUtils.getCookie(request, "token");
        if (tokenCookie == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = tokenCookie.getValue();
        MemberResponse member = memberService.getMemberFromToken(token);
        if (member == null || !member.role().equals("ADMIN")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
