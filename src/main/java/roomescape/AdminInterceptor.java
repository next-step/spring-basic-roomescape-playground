package roomescape;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import org.springframework.stereotype.Component;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(401);
            return false;
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
            response.setStatus(401);
            return false;
        }

        String memberId = jwtTokenProvider.getMemberId(token);
        Member member = memberService.findById(Integer.parseInt(memberId));

        if (!"ADMIN".equals(member.getRole())) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

}