package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import roomescape.auth.dto.MemberInfo;
import roomescape.auth.exception.InvalidTokenException;
import roomescape.member.MemberService;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminHandlerInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie tokenCookie = WebUtils.getCookie(request, "token");
        if (tokenCookie == null || tokenCookie.getValue() == null) {
            throw new InvalidTokenException("Token is required.");
        }

        Long id = jwtTokenProvider.validateToken(tokenCookie.getValue());
        MemberInfo memberInfo = MemberInfo.from(memberService.loadMember(id));

        if (!memberInfo.role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
