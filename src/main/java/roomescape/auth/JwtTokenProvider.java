package roomescape.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.ExpiredTokenException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKeyString,
            @Value("${jwt.access-token-validity-in-milliseconds}") long validityInMilliseconds
    ) {
        this.validityInMilliseconds = validityInMilliseconds;
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKeyString);
        this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String createToken(Long id, String name, String role) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(id.toString())
                .claim("name", name)
                .claim("role", role)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long validateToken(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("The token is expired.");
        } catch (Exception e) {
            throw new RuntimeException("Invalid token.");
        }
    }
}
