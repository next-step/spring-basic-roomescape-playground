package roomescape.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${roomescape.auth.jwt.secret}") String secretKey,
            @Value("${roomescape.auth.jwt.expire-length:3600000}") long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expire)
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(key)
                .compact();
    }

    public Long extractMemberIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }
}
