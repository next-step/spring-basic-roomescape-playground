package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long expirationMillis;

    public JwtTokenProvider(
            @Value("${roomescape.auth.jwt.secret}") String rawSecretKey,
            @Value("${roomescape.auth.jwt.secret.expiration}") long expirationMillis
    ) {
        final byte[] keyBytes = Base64.getDecoder().decode(rawSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = expirationMillis;
    }

    public String createToken(Long memberId, String name, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("name", name)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public Long findMemberId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Member findMember(String token) {
        Claims claims = getClaims(token);
        Long id = Long.valueOf(claims.getSubject());
        String name = claims.get("name", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));
        return new Member(id, name, null, role);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
