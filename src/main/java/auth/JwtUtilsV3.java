package auth;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import roomescape.member.Member;

public class JwtUtilsV3 {

    private final String issuer;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtUtilsV3(String secretKey, String issuer) {
        this.issuer = issuer;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .requireIssuer(this.issuer)
                .build();
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
        return Long.valueOf(jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public String getRoleByToken(String token) {
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
