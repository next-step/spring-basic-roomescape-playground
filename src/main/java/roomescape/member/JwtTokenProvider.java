package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long expirationMilliseconds;

    public JwtTokenProvider(@Value("${roomescape.auth.jwt.secret}") String secretKey,
                            @Value("${roomescape.auth.jwt.expiration-milliseconds}") long expirationMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMilliseconds = expirationMilliseconds;
    }

    public String createToken(Long memberId) {
        Instant issuedAt = Instant.now();
        return Jwts.builder()
                .setSubject(memberId.toString())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(issuedAt.plusMillis(expirationMilliseconds)))
                .signWith(secretKey)
                .compact();
    }

    public TokenPayload parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (claims.getId() == null || claims.getExpiration() == null) {
            throw new JwtException("토큰 식별자 또는 만료 시간이 없습니다.");
        }

        return new TokenPayload(
                Long.valueOf(claims.getSubject()),
                claims.getId(),
                claims.getExpiration().toInstant()
        );
    }
}
