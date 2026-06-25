package roomescape.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JwtTokenProvider {
    private final Key secretKey;
    private final long tokenExpirationInMilliseconds;

    public JwtTokenProvider(@Value("${roomescape.auth.jwt.secret}") String rawSecretKey,
                            @Value("${roomescape.auth.jwt.expiration}") Long expiration) {
        final byte[] keyBytes = Base64.getDecoder().decode(rawSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.tokenExpirationInMilliseconds = expiration;
    }

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationInMilliseconds))
                .signWith(secretKey)
                .compact();
    }

    public Claims getTokenPayload(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            checkExpiration(claims);
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다");
        }
    }

    private void checkExpiration(Claims claims) {
        if (claims.getExpiration().before(new Date())) {
            throw new IllegalStateException("유효하지 않은 토큰입니다");
        }
    }
}
