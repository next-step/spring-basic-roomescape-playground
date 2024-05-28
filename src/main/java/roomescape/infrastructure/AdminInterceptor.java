package roomescape.infrastructure;

import auth.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Member;
import roomescape.member.MemberService;

public class AdminInterceptor implements HandlerInterceptor {

    private JwtUtils jwtUtils;

    public AdminInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = jwtUtils.extractTokenFromCookie(cookies);
        String role = jwtUtils.extractClaim(token, "role");

        if (!"ADMIN".equals(role)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

}
