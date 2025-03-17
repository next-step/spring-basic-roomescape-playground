package roomescape.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.token.cookie.CookieResolver;
import roomescape.auth.token.jwt.JwtProperties;
import roomescape.auth.token.jwt.JwtResolver;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtResolver jwtResolver;
    private final CookieResolver cookieResolver;

    public AuthInterceptor(JwtResolver jwtResolver, final CookieResolver cookieResolver) {
        this.jwtResolver = jwtResolver;
        this.cookieResolver = cookieResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = cookieResolver.getToken(request.getCookies());
        String name = jwtResolver.getName(token);
        String role = jwtResolver.getRole(token);

        if (name == null || role == null || !role.equals(JwtProperties.ADMIN)) {
            response.setStatus(404);
            return false;
        }
        return true;
    }
}
