package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.jwt.TokenExtractor;
import roomescape.auth.jwt.TokenProvider;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    public AdminAuthorizationInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        try {
            String token = TokenExtractor.extractToken(request);

            if (token == null || token.isBlank()) {
                return unauthorized(response);
            }

            LoginMember member = tokenProvider.parseLoginMember(token);

            if (!"ADMIN".equals(member.role())) {
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
