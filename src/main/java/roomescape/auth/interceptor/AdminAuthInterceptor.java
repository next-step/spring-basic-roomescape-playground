package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.CookieManager;
import roomescape.auth.service.AuthService;
import roomescape.exception.ExceptionMessage;
import roomescape.exception.UnAuthorizedException;
import roomescape.member.domain.Member;

import static roomescape.auth.controller.AuthController.AUTH_TOKEN_COOKIE;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminAuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        CookieManager cookieManager = new CookieManager(request.getCookies());
        String accessToken = cookieManager.getValue(AUTH_TOKEN_COOKIE);

        Member member = authService.getLoginMember(accessToken);
        if (member.isAdmin()) {
            return true;
        }
        throw new UnAuthorizedException(ExceptionMessage.UNAUTHORIZED_MEMBER.getMessage());
    }
}
