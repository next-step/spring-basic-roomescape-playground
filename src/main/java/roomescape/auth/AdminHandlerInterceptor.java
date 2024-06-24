package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    public AdminHandlerInterceptor(final JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        final String token = CookieUtil.getTokenFromCookie(request, AuthConfig.TOKEN.getKey());
        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        final String role = jwtTokenManager.getValueFromJwtToken(token, AuthConfig.ROLE.getKey());
        if (!role.equalsIgnoreCase(AuthConfig.ADMIN.getKey())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
