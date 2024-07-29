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

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class RoleCheckInterceptor implements HandlerInterceptor {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RoleCheck roleCheck = handlerMethod.getMethodAnnotation(RoleCheck.class);
        if (roleCheck == null) {
            return true;
        }

        Cookie[] cookies = getCookies(request);
        String token = extractTokenFromCookie(cookies);

        if (token.isEmpty()) {
            int statusCode = UNAUTHORIZED.value();
            response.setStatus(statusCode);
            return false;
        }

        Role requiredRole = roleCheck.role();
        Role memberRole = parseMemberRole(token);
        if (memberRole == Role.ADMIN) {
            return true;
        }

        if (isNotRoleExact(requiredRole, memberRole)) {
            response.setStatus(FORBIDDEN.value());
            return false;
        }

        return true;
    }

    private boolean isNotRoleExact(Role requiredRole, Role memberRole) {
        return requiredRole != memberRole;
    }

    private Cookie[] getCookies(HttpServletRequest request) {
        return request.getCookies();
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

    private Role parseMemberRole(String token) {
        return Role.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class));
    }
}
