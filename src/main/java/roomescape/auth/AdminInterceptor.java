package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.LoginMember;
import roomescape.member.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final CookieTokenExtractor cookieTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AdminInterceptor(CookieTokenExtractor cookieTokenExtractor, JwtTokenProvider jwtTokenProvider,
                            MemberService memberService) {
        this.cookieTokenExtractor = cookieTokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = cookieTokenExtractor.extract(request.getCookies());
        String email = jwtTokenProvider.getPayload(token);
        LoginMember member = memberService.findLoginMemberByEmail(email);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

}
