package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.InvalidTokenFormatException;
import roomescape.member.Member;

@Component
public class TokenGenerator {
    private static final Logger log = LoggerFactory.getLogger(TokenGenerator.class);
    private static final String USER_NAME = "name";
    private static final String USER_ROLE = "role";

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public String generateAccessToken(Member member) {
        Claims customClaims = createClaims(member);

        String token = Jwts.builder()
                .setClaims(customClaims)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return token;
    }

    public AuthCredential parseAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = Long.valueOf(claims.getSubject());
            String name =  claims.get(USER_NAME, String.class);
            String role = claims.get(USER_ROLE, String.class);

            return new AuthCredential(id, name, role);
        } catch (SignatureException e) {
            throw new InvalidTokenFormatException("Invalid JWT signature");
        }
    }

    private Claims createClaims(Member member) {
        Claims customClaims = Jwts.claims();
        customClaims.setSubject(member.getId().toString());
        customClaims.put(USER_NAME, member.getName());
        customClaims.put(USER_ROLE, member.getRole());
        return customClaims;
    }
}
