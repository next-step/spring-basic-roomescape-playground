package roomescape.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.auth.jwt.CookieTokenExtractor;
import roomescape.global.auth.jwt.JwtTokenProvider;
import roomescape.global.excpetion.UnauthorizedException;

public class RoleInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final String requiredRole;

    public RoleInterceptor(JwtTokenProvider jwtTokenProvider, String requiredRole) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieTokenExtractor.extract(request);
        String role = jwtTokenProvider.getRole(token);

        if (!requiredRole.equals(role)) {
            throw new UnauthorizedException();
        }

        return true;
    }
}
