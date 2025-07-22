package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import roomescape.member.Member;

public class JwtUtilsV2 {

    private final String issuer;
    private final SecretKey secretKey;

    public JwtUtilsV2(String secretKey, String issuer) {
        this.issuer = issuer;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(secretKey)
                .compact();
    }

    public Long getMemberIdByToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    public String getRoleByToken(String token) {
        return String.valueOf(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody().get("role", String.class));
    }

}

