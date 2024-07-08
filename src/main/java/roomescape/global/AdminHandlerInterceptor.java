package roomescape.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import auth.JwtUtils;
import roomescape.global.constant.AuthConstant;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    public AdminHandlerInterceptor(final JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        final String token = CookieUtil.getTokenFromCookie(request, AuthConstant.TOKEN);
        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        final String role = jwtUtils.getValueFromJwtToken(token, AuthConstant.ROLE);
        if (!role.equalsIgnoreCase(AuthConstant.ADMIN.getValue())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
