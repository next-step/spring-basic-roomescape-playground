package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class TokenProvider {

    private final String secretKey;

    public TokenProvider(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public Claims parseToken(String token){
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
