package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtils {
    private final SecretKey key;

    public JwtUtils(String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Long id, String name, String email, String role) {
        return Jwts.builder()
                   .setSubject(id.toString())
                   .claim("name", name)
                   .claim("email", email)
                   .claim("role", role)
                   .signWith(key)
                   .compact();
    }

    public Long getId(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public String getName(String token) {
        return getClaims(token).get("name", String.class);
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}
