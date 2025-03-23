package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.CookieManager;
import roomescape.auth.AuthService;
import roomescape.member.LoginMember;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final CookieManager cookieManager;

    public RoleInterceptor(AuthService authService, CookieManager cookieExtractor) {
        this.authService = authService;
        this.cookieManager = cookieExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = cookieManager.getTokenFrom(request);

        LoginMember member = authService.getLoginMemberFromToken(token);

        if (member.isAdmin()) {
            return true;
        }

        throw new RuntimeException("권한 없음");
    }

}
