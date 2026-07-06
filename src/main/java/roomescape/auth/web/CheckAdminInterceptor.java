package roomescape.auth.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.auth.service.AuthService;
import roomescape.exception.ApplicationException;
import roomescape.util.CookieUtil;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;
    private final AuthService authService;

    public CheckAdminInterceptor(CookieUtil cookieUtil, AuthService authService) {
        this.cookieUtil = cookieUtil;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS);
        }

        String token = cookieUtil.extractToken(cookies)
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS));

        LoginMember member = authService.findAuthenticatedMember(token);
        if (member == null || !member.isAdmin()) {
            throw new ApplicationException(AuthErrorCode.UNAUTHORIZED_REQUEST);
        }

        return true;
    }
}
