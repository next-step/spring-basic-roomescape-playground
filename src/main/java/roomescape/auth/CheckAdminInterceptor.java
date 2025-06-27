package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieValueExtractor cookieValueExtractor;

    public CheckAdminInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider,
                                 CookieValueExtractor cookieValueExtractor) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieValueExtractor = cookieValueExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = cookieValueExtractor.extractToken(cookies);
        Long memberId = jwtTokenProvider.getMemberIdByToken(token);
        Member member = memberService.getMemberById(memberId);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(403);
            return false;
        }
        return true;
    }

}
