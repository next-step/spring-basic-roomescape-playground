package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.ApiException;
import roomescape.member.Member;

@Component
public class AdminPageAuthorizeInterceptor implements HandlerInterceptor {
    private final AuthorizationService authorizationService;

    public AdminPageAuthorizeInterceptor(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        AuthorizedMember member = authorizationService.tryAuthorizeRequest(request);

        if (member == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (member.role() != Member.Role.ADMIN) {
            throw ApiException.status(HttpStatus.UNAUTHORIZED);
        }

        return true;
    }
}
