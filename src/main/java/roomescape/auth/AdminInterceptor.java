package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final CookieExtractor cookieExtractor;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public AdminInterceptor(CookieExtractor cookieExtractor, JwtProvider jwtProvider, MemberService memberService) {
        this.cookieExtractor = cookieExtractor;
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = cookieExtractor.extractTokenFromCookie(request.getCookies());

        if (token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Long memberId = jwtProvider.extractMemberId(token);
        Member member = memberService.findById(memberId);

        if (!member.getRole().equals("ADMIN")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
