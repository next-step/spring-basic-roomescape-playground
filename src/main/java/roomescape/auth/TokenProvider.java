package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class TokenProvider {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(SIGNING_KEY)
                .compact();

        return accessToken;
    }

    public Long extractMemberId(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());

        return memberId;
    }
}
