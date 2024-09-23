package roomescape.auth;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.LoginMember;
import roomescape.member.Member;

@Component
public class JwtProvider {

    private final String secretKey;
    private final Long validityInMilliseconds;

    public JwtProvider(
        @Value("${security.jwt.token.secret-key}") String secretKey,
        @Value("${security.jwt.token.expire-length}") Long validityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("member cannot be null");
        }
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("email", member.getEmail())
            .claim("role", member.getRole())
            .setExpiration(Date.from(Instant.now().plusMillis(validityInMilliseconds)))
            .signWith(getSecretKey())
            .compact();
    }

    public LoginMember getLoginMember(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return new LoginMember(
            Long.parseLong(claims.getSubject()),
            claims.get("name").toString(),
            claims.get("email").toString(),
            claims.get("role").toString()
        );
    }

    private SecretKey getSecretKey() {
        String encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(encoded.getBytes());
    }
}
