package roomescape.auth.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.service.AuthService;
import roomescape.exception.ApplicationException;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final TokenExtractor tokenExtractor;
    private final AuthService authService;

    public CheckAdminInterceptor(TokenExtractor tokenExtractor, AuthService authService) {
        this.tokenExtractor = tokenExtractor;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = tokenExtractor.extractAccessToken(
                request.getCookies()
        );

        LoginMember member = authService.findAuthenticatedMember(token);
        if (member == null || !member.isAdmin()) {
            throw new ApplicationException(AuthErrorCode.UNAUTHORIZED_REQUEST);
        }

        return true;
    }
}
