package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.member.MemberResponse;
import roomescape.member.Role;
import roomescape.util.TokenExtractor;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminAuthorizationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = TokenExtractor.extractTokenFromCookie(request);
        MemberResponse memberResponse = authService.getMemberByToken(token);

        if (memberResponse.role() != Role.ADMIN) {
            throw new RoomEscapeException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return true;
    }
}
