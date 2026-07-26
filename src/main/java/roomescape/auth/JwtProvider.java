package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;
import roomescape.member.Role;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtProvider {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtProvider(String secret, long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String createToken(Member member) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole().name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public LoginMember extractLoginMember(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new LoginMember(
                Long.valueOf(claims.getSubject()),
                claims.get("name", String.class),
                Role.valueOf(claims.get("role", String.class))
        );
    }
}
