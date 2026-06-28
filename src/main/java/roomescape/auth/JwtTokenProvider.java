package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(getSecretKey())
                .compact();
    }

    public Long extractMemberId(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.valueOf(subject);
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
