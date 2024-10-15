package auth;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import roomescape.member.dto.LoginMember;
import roomescape.member.model.Member;

@RequiredArgsConstructor
public class JwtProvider {

    private final String secretKey;
    private final Long validityInMilliseconds;

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
