package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import missionAuth.JwtDto;
import missionAuth.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.MemberService;
import roomescape.member.Role;


@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtTokenProvider;

    public AdminAuthInterceptor(JwtUtils jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request.getCookies());
        if (token == null || token.isBlank()) {
            response.setStatus(401);
            return false;
        }

        try {
            JwtDto dto = jwtTokenProvider.parse(token);
            if (dto.role() != Role.ADMIN) {
                response.setStatus(401);
                return false;
            }
            return true;

        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }
}
