package roomescape.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.dto.LoginMember;
import roomescape.member.Member;

@Component
public class TokenProvider {

    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public TokenProvider(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder()
            .setSigningKey(this.secretKey)
            .build();
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .signWith(secretKey)
            .compact();
    }

    public LoginMember parseLoginMember(String token) {
        Claims claims = parseToken(token);
        Long id = Long.valueOf(claims.getSubject());
        String name = claims.get("name", String.class);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return new LoginMember(id, name, email, role);
    }

    public Claims parseToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
