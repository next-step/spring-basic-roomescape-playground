package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.Role;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final CookieManager cookieExtractor;
    private final JwtProvider jwtProvider;

    public AdminInterceptor(CookieManager cookieExtractor, JwtProvider jwtProvider) {
        this.cookieExtractor = cookieExtractor;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = cookieExtractor.extractTokenFromCookie(request.getCookies());

        if (token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        LoginMember loginMember = jwtProvider.extractLoginMember(token);

        if (loginMember.role() != Role.ADMIN) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
