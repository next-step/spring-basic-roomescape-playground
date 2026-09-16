package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.member.AuthorizationException;
import roomescape.member.Member;

@Component
public class TokenUtil {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final String TOKEN_COOKIE_NAME = "token";

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Long getMemberId(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (RuntimeException e) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
    }

    public String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
