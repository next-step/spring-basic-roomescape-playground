package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public AdminInterceptor(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = authorizationExtractor.extract(request);
        LoginMember loginMember = authService.createAuthentication(token);

        if (!loginMember.role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
