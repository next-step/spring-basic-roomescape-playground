package roomescape.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JwtUtil {

    private final String secretKey;
    private final JwtParser jwtParser;
    private final Key key;

    public JwtUtil(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .signWith(key)
            .compact();
    }

    public JwtPayload parseToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return new JwtPayload(
            String.valueOf(claims.get("name")),
            String.valueOf(claims.get("role"))
        );
    }
}
