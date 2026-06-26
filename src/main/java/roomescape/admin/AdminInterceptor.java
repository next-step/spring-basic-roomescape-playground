package roomescape.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.MemberService;
import roomescape.token.JwtTokenProvider;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String token = memberService.extractTokenFromCookie(cookies);
        Claims claims = jwtTokenProvider.getTokenPayload(token);

        if (!claims.get("role", String.class).equals("ADMIN")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }
}
