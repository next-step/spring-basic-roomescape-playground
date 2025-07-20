package roomescape.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.util.CookieUtil;
import roomescape.global.util.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Component
public class CustomInterceptor implements HandlerInterceptor {
    private static final String ADMIN_ROLE = "ADMIN";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public CustomInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = CookieUtil.extractTokenFromCookies(request.getCookies());

        if (token == null || token.isBlank()) {
            return false;
        }

        Long memberId = jwtTokenProvider.getMemberId(token);
        Member member = memberService.findById(memberId);

        if (member == null || !member.getRole().equals(ADMIN_ROLE)) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
