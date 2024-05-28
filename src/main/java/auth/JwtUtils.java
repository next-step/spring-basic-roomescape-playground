package auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.AuthorizationException;
import roomescape.member.LoginRequest;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final MemberRepository memberRepository;

    @Value("${roomescape.auth.jwt.secret}")
    private String secret;

    public String createToken(Long id, String name, String role) {
        String secretKey = secret;
        String accessToken = Jwts.builder()
                .setSubject(id.toString())
                .claim("name", name)
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return accessToken;
    }

    public Long extractIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

}
