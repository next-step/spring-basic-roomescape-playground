package roomescape.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.util.CookieTokenProvider;
import roomescape.member.AuthService;
import roomescape.member.Member;

import java.util.Optional;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final CookieTokenProvider cookieTokenProvider;

    public AuthorizationInterceptor(AuthService authService, CookieTokenProvider cookieTokenProvider) {
        this.authService = authService;
        this.cookieTokenProvider = cookieTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<String> tokenOptional = cookieTokenProvider.extractToken(request.getCookies());

        if (tokenOptional.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Member member = authService.loginCheck(tokenOptional.get());

        if (member == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (member.isNotAdmin()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
