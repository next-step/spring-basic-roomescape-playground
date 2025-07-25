package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public AdminAuthorizationInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        try {
            Claims claims = TokenUtils.getClaimsFromCookies(request.getCookies(), tokenProvider);
            String role = claims.get("role", String.class);

            if (!"ADMIN".equals(role)) {
                return unauthorized(response);
            }
            return true;
        } catch (Exception e) {
            return unauthorized(response);
        }
    }

    private boolean unauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
