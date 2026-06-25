package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.ApiException;
import roomescape.member.Member;

@Component
public class AdminPageAuthorizeInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AdminPageAuthorizeInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthorizedMember member = authService.tryAuthenticateRequest(request);

        if (member == null) {
            throw ApiException.status(HttpStatus.UNAUTHORIZED);
        }

        if (member.role() != Member.Role.ADMIN) {
            throw ApiException.status(HttpStatus.FORBIDDEN);
        }

        return true;
    }
}
