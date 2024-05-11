package roomescape.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.util.CookieUtil;
import roomescape.util.JwtUtil;

@Component
public class RoleHandlerInterceptor implements HandlerInterceptor {
    private CookieUtil cookieUtil;
    private JwtUtil jwtUtil;

    public RoleHandlerInterceptor(CookieUtil cookieUtil, JwtUtil jwtUtil) {
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = cookieUtil.extractTokenFromCookie(request);
        String role = jwtUtil.getRole(token);
        if (!role.equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }



}
