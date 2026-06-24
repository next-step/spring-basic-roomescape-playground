package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final Key secretKey;

    public JwtTokenProvider(@Value("${roomescape.auth.jwt.secret}") String rawSecretKey) {
        final byte[] keyBytes = Base64.getDecoder().decode(rawSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Claims getTokenPayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 토큰입니다");
        }
    }
}
