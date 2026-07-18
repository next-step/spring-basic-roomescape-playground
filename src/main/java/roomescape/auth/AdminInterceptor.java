package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.member.Role;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final CookieManager cookieExtractor;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public AdminInterceptor(CookieManager cookieExtractor, JwtProvider jwtProvider, MemberService memberService) {
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

        if (!(member.getRole() == Role.ADMIN)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
