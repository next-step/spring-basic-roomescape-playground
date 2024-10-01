package roomescape.global.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.global.exception.AuthenticationException;
import roomescape.member.domain.Member;

@Component
public class JwtProvider {

    private final String secretKey;

    public JwtProvider(
        @Value("${auth.jwt.secret}") String secretKey
    ) {
        this.secretKey = secretKey;
    }

    public String createToken(Member member) {
        Key key = getSecretKey();
        return Jwts.builder()
            .signWith(key)
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .compact();
    }

    public Long getMemberId(String token) {
        try {
            String memberId = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

            return Long.valueOf(memberId);
        } catch (JwtException e) {
            throw new AuthenticationException();
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
