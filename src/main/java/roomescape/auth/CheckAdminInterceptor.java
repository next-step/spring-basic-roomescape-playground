package roomescape.auth;

import auth.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtUtils jwtUtils;
    private final CookieValueExtractor cookieValueExtractor;

    public CheckAdminInterceptor(MemberService memberService, JwtUtils jwtUtils,
                                 CookieValueExtractor cookieValueExtractor) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
        this.cookieValueExtractor = cookieValueExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = cookieValueExtractor.extractToken(cookies);
        Long memberId = jwtUtils.getMemberIdByToken(token);
        Member member = memberService.getMemberById(memberId);

        if (member == null || !member.isAdmin()) {
            response.setStatus(403);
            return false;
        }
        return true;
    }

}
