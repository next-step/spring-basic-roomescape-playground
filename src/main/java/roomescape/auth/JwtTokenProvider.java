package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long validityInMilliseconds;
    private final TimeProvider timeProvider;

    public JwtTokenProvider(JwtProperties jwtProperties, TimeProvider timeProvider) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
        this.validityInMilliseconds = jwtProperties.getValidityInMilliseconds();
        this.timeProvider = timeProvider;
    }

    public String createToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        Date now = timeProvider.now();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
