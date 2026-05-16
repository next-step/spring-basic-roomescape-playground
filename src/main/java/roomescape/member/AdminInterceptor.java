package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractTokenFromCookie(request);

        if (token == null || token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Member member = memberService.findByToken(token);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
