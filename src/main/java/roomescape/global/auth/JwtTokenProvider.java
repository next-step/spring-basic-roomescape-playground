package roomescape.global.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.global.auth.exception.AuthenticationException;
import roomescape.member.Member;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    private final Long expirationTime;

    public JwtTokenProvider(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token.expiration-time}") Long expirationTime
    ) {
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
    }

    public String createToken(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("유저가 없습니다");
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(getKey())
            .compact();
    }

    public Long getMemberId(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
            );
        } catch (JwtException e) {
            throw new AuthenticationException();
        }
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
