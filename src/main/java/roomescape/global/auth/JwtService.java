package roomescape.global.auth;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.Member;

@Service
public class JwtService {
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long decodeToken(String token) {
        if (token == null) return null;
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return cookie.getValue();
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }
}
