package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final String secretKey;

    public JwtProvider(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(MemberResponse memberResponse) {
        return Jwts.builder()
                .setSubject(memberResponse.getId().toString())
                .claim("name", memberResponse.getName())
                .claim("email", memberResponse.getEmail())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long parseMemberIdFrom(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

}
