package roomescape.auth;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.util.JwtUtil;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AdminInterceptor(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request.getCookies());

        if (token == null) {
            sendUnauthorized(response, "로그인이 필요합니다.");
            return false;
        }

        try {
            Long memberId = jwtUtil.getMemberIdFromToken(token);

            Member member = memberService.findById(memberId);

            if (!member.isAdmin()) {
                sendUnauthorized(response, "관리자 권한이 없습니다.");
                return false;
            }
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            sendUnauthorized(response, "유효하지 않은 토큰입니다.");
            return false;
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) {
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
