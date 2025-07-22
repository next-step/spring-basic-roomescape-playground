package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.member.Role;
import roomescape.util.TokenExtractor;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AdminAuthorizationInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = TokenExtractor.extractTokenFromCookie(request);
        Role role = jwtProvider.extractRole(token);

        if (role != Role.ADMIN) {
            throw new RoomEscapeException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return true;
    }
}
