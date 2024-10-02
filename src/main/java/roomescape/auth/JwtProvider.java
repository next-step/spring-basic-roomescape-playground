package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.model.Member;

public class JwtProvider {

    private final String secretKey;

    public JwtProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public String getNameFromToken(String token) {
        return (String)Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().get("name");
    }

    public Long getIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().getSubject());
    }
}
