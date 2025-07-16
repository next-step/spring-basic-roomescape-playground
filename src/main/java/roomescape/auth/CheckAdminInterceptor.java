package roomescape.auth;

import auth.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final CookieValueExtractor cookieValueExtractor;

    public CheckAdminInterceptor(JwtUtils jwtUtils, CookieValueExtractor cookieValueExtractor) {
        this.jwtUtils = jwtUtils;
        this.cookieValueExtractor = cookieValueExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = cookieValueExtractor.extractToken(cookies);
        if (!jwtUtils.getRoleByToken(token).equals("ADMIN")) {
            response.setStatus(403);
            return false;
        }
        return true;
    }

}
