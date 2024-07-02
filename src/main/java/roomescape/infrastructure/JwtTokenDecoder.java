package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenDecoder {
    private static String secretKey;

    @Value("${roomescape.auth.jwt.secret}")
    public void setSecretKey(String secretKey) {
        JwtTokenDecoder.secretKey = secretKey;
    }
    public static Long decodeToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        return memberId;
    }
}
