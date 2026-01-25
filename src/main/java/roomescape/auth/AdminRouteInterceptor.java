package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminRouteInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AdminRouteInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        if (!method.hasMethodAnnotation(AdminRoute.class) && !method.getBeanType()
                .isAnnotationPresent(AdminRoute.class)) {
            return true;
        }

        String token = JwtTokenProvider.extractTokenFromCookies(request.getCookies());

        if (token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        Claims claims = jwtTokenProvider.getClaims(token);
        String role = claims.get("role", String.class);

        if (!Role.ADMIN.name().equals(role)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }
}
