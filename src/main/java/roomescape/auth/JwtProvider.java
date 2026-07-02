package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtProvider(
            @Value("${roomescape.auth.jwt.secret}") String secret,
            @Value("${roomescape.auth.jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String createToken(Member member) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public Long extractMemberId(String token) {
        return Long.valueOf(
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}
