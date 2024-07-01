package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.cookie.CookieService;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;
import roomescape.member.model.Member;
import roomescape.member.model.MemberLoginResponse;

@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String token = CookieService.extractTokenFromCookie(request.getCookies());
            Member member = memberService.checkLogin(token);

            if (member == null || !member.getRole().equals("ADMIN")) {
                response.setStatus(401);
                return false;
            }

            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
