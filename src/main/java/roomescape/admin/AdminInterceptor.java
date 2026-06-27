package roomescape.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.MemberRole;
import roomescape.token.CookieTokenUtils;
import roomescape.token.JwtTokenProvider;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieTokenUtils cookieTokenExtractor;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider, CookieTokenUtils cookieTokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieTokenExtractor = cookieTokenExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = cookieTokenExtractor.extractToken(request);
        Claims claims = jwtTokenProvider.getTokenPayload(token);

        if (!claims.get("role", String.class).equals(MemberRole.ADMIN.toString())) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }
}
