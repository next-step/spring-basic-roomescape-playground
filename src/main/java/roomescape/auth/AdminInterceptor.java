package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Role;

import java.util.Optional;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final CookieTokenExtractor cookieTokenExtractor;

    public AdminInterceptor(AuthService authService, CookieTokenExtractor cookieTokenExtractor) {
        this.authService = authService;
        this.cookieTokenExtractor = cookieTokenExtractor;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        Optional<String> token = cookieTokenExtractor.extract(request);

        if (token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        LoginMember loginMember = authService.findMemberByToken(token.get());

        if (loginMember.role() != Role.ADMIN) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }
}