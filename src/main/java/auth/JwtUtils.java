package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;

public class JwtUtils {

    private final String secretKey;
    private final String issuer;

    public JwtUtils(String secretKey, String issuer) {
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long getMemberIdByToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    public String getRoleByToken(String token) {
        return String.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .requireIssuer(issuer)
                .build()
                .parseClaimsJws(token)
                .getBody().get("role", String.class));
    }

}
