package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final String AUTH_TOKEN_COOKIE = "token";

    public AdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        CookieManager cookieManager = new CookieManager(request.getCookies());
        String accessToken = cookieManager.getValue(AUTH_TOKEN_COOKIE);

        if (accessToken == null || authService.isTokenInvalid(accessToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
            return false;
        }

        Member member = authService.getLoginMember(accessToken);
        if (member == null || !"ADMIN".equals(member.getRole())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
            return false;
        }
        return true;
    }
}
