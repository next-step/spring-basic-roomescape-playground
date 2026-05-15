package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Key secertKey;

    public JwtTokenProvider(
            @Value("${roomescape.auth.jwt.secret}") String rawSecretKey
    ) {
        final byte[] keyBytes = Base64.getDecoder().decode(rawSecretKey);
        this.secertKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Long memberId, String name, String role) {

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("name", name)
                .claim("role", role)
                .signWith(secertKey)
                .compact();
    }

    public Long findMemberId(String token) {

        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(secertKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
