package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.member.AuthorizationException;
import roomescape.member.Member;

@Component
public class TokenUtil {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Long getMemberId(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (RuntimeException e) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
    }
}
