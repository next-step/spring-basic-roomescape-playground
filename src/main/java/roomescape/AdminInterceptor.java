package roomescape;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.member.Role;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnauthorizedException("쿠키가 없습니다");
        }

        boolean find = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
                find = true;
                break;
            }
        }

        if (!find) {
            throw new UnauthorizedException("관리자 권한이 없습니다");
        }

        String memberId = jwtTokenProvider.getMemberId(token);
        Member member = memberService.findById(Long.parseLong(memberId));

        if (member.getRole() != Role.ADMIN) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
