package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    public static final String HEADER_TYPE = "typ";
    public static final String TOKEN_TYPE = "JWT";
    private final String secretKey;

    public JwtProvider(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(MemberResponse memberResponse) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .setHeaderParam(HEADER_TYPE, TOKEN_TYPE)
                .setSubject(memberResponse.getId().toString())
                .compact();
    }

    public Long parseMemberIdFrom(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

}
