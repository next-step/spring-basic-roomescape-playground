package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JwtProvider {
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public Long extractMemberId(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    public String createToken(Member member) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                .compact();
        return accessToken;
    }
}
