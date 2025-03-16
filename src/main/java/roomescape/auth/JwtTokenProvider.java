package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Value("${roomescape.jwt.token.expire-length}")
    private long validityInMilliseconds;

    private static final String CLAIM_KEY_EMAIL = "email";

    public String createToken(String email) {

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.claims();
        claims.put(CLAIM_KEY_EMAIL, email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get(CLAIM_KEY_EMAIL, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token provided", e);
        }
    }

    public Long getIdFromToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String idString = claims.get("id", String.class);
        return Long.valueOf(idString);
    }

    public boolean isTokenValid(String accessToken) {
        if (accessToken == null || accessToken.trim().isEmpty()) {
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenInvalid(String accessToken) {
        return !isTokenValid(accessToken);
    }
}
