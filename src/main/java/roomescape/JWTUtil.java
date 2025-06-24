package roomescape;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JWTUtil {

    private static final String SECRET = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String createToken(Member member) {
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //테스트용
    public static String createToken(String email, String password) {
        if ("admin@email.com".equals(email) && "password".equals(password)) {
            Member admin = new Member(1L, "어드민", email, "ADMIN");
            return createToken(admin);
        }
        throw new IllegalArgumentException("Invalid credentials for test token creation.");
    }
}
