package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.util.JwtUtil;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request.getCookies());

        if (token == null) {
            response.setStatus(401);
            return false;
        }

        try {
            Long memberId = JwtUtil.getMemberIdFromToken(token);

            Member member = memberService.findById(memberId);

            if (!member.isAdmin()) {
                response.setStatus(401);
                return false;
            }
            return true;

        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
