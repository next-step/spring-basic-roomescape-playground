package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.member.Role;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AdminAuthInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request.getCookies());
        if (token == null || token.isBlank()) {
            response.setStatus(401);
            return false;
        }

        try {
            Long memberId = jwtTokenProvider.extractMemberIdFromToken(token);
            Member member = memberService.findById(memberId);

            if (member == null || member.getRole() != Role.ADMIN) {
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
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }
}
