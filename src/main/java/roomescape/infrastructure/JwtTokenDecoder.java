package roomescape.infrastructure;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTokenDecoder {
    public static Long decodeToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        return memberId;
    }
}
