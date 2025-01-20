package roomescape.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final long EXPIRATION_TIME = 86400000;
    @Value("${jwt.secret}")
    private String secretKey;

    private Key generateKey() {
        return new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(Long userId, String role) {
        Key key = generateKey();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            Key key = generateKey();
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.", e);
        }
    }
}
