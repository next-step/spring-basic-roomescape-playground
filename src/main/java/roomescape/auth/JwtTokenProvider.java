package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    static final String CLAIM_USER_ID = "id";

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Value("${roomescape.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(long id) {

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.claims();
        claims.put(CLAIM_USER_ID, String.valueOf(id));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String idString = claims.get(CLAIM_USER_ID, String.class);
            return Long.valueOf(idString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid token provided", e);
        }
    }
}
