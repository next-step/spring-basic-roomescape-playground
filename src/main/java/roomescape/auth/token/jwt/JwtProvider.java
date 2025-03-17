package roomescape.auth.token.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    public String generateToken(Long id, String name, String role) {
        return Jwts.builder()
                .setSubject(id.toString())
                .claim(JwtProperties.NAME, name)
                .claim(JwtProperties.ROLE, role)
                .signWith(Keys.hmacShaKeyFor(JwtProperties.SECRET_KEY.getBytes()))
                .compact();
    }
}
