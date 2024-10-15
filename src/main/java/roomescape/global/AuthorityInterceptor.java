package roomescape.global;

import static roomescape.auth.CookiesUtils.extractTokenFromCookie;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.JwtProvider;

@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AuthorityInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        Exception {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        Long id = jwtProvider.getIdFromToken(token);
        String role = jwtProvider.getRoleFromToken(token);

        if (id == null || !role.equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
