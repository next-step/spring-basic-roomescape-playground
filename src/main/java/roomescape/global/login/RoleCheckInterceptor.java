package roomescape.global.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleCheckInterceptor implements HandlerInterceptor {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RoleCheck roleCheck = handlerMethod.getMethodAnnotation(RoleCheck.class);

        if (roleCheck == null) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        if (token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        String role = parseMemberRole(token);

        if (Role.valueOf(role).ordinal() < roleCheck.role().ordinal()) {
            response.setStatus(403);
            return false;
        }

        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

    private String parseMemberRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
