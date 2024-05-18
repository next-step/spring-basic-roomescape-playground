package roomescape.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.util.CookieUtil;
import roomescape.util.JwtProvider;

@Component
public class RoleHandlerInterceptor implements HandlerInterceptor {

    private JwtProvider jwtProvider;

    public RoleHandlerInterceptor( JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtil.extractTokenFromCookie(request);
        String role = jwtProvider.getRole(token);
        if (!role.equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }



}
