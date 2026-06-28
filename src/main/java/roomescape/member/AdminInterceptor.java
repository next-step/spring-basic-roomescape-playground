package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final String secretKey;

    public AdminInterceptor(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        try {
            String token = extractTokenFromCookie(cookies);

            String role = Jwts.parserBuilder() //그냥 토큰에서 Role정보를 빼오는 방식 DB 조회를 하지 않고
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);

            if (role == null) {
                throw new IllegalStateException("토큰 내 권한 정보(Role)가 존재하지 않습니다.");
            }

            String requestURI = request.getRequestURI();

            if (requestURI.startsWith("/admin")) {
                if (!"ADMIN".equals(role)) {
                    response.setStatus(403);
                    return false;
                }
                return true;
            }

            if (requestURI.startsWith("/reservations")) {
                if ("ADMIN".equals(role) || "USER".equals(role)) {
                    return true;
                }
            }

            return true;
        } catch (Exception e) {
            response.setStatus(401); //미인증
            return false;
        }
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException("요청에 쿠키 배열이 존재하지 않습니다.");
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("쿠키 중 인증 토큰('token')을 찾을 수 없습니다.");
    }
}
