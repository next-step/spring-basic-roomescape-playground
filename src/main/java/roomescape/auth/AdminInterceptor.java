package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    public AdminInterceptor(TokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request.getCookies());

        Long memberId = tokenProvider.extractMemberId(token);

        Member member = memberService.getMemberById(memberId);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new RuntimeException("인증 정보가 존재하지 않습니다.");
        }

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new RuntimeException("토큰이 존재하지 않습니다.");
    }
}
