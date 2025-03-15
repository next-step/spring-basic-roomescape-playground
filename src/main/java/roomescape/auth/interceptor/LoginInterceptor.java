package roomescape.auth.interceptor;

import static roomescape.auth.util.AuthUtil.extractToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.service.AuthService;
import roomescape.auth.dto.MemberDetailResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request);

        if (token == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        MemberDetailResponse memberResponse = authService.checkLogin(token);
        LoginMember member = new LoginMember(memberResponse.id(), memberResponse.name(), memberResponse.email(), memberResponse.role());

        if (!member.isAdmin()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}
