package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.member.Member;

@Service
public class JwtTokenService {

    private final String secretKey =  "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String getAccessToken(Long id, String name, String role) {
        return Jwts.builder()
            .setSubject(id.toString())
            .claim("name", name)
            .claim("role", role)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public Long getMemberId(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                         .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                         .build()
                         .parseClaimsJws(token)
                         .getBody().getSubject());
    }

}
