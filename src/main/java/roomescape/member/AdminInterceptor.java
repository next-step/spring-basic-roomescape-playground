package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        try {
            String token = extractToken(cookies);

            String role = Jwts.parserBuilder() //그냥 토큰에서 Role정보를 빼오는 방식 DB 조회를 하지 않고
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);

            if (role == null || !"ADMIN".equals(role)) {
                response.setStatus(401);
                return false;
            }

            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    private String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new IllegalArgumentException();
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException();
    }
}
