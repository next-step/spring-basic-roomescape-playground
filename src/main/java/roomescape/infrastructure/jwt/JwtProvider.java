package roomescape.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.auth.AuthorizationProvider;
import roomescape.auth.MemberAuthContext;
import roomescape.auth.MemberCredential;

@Component
public class JwtProvider implements AuthorizationProvider {

    private static final String USER_NAME = "name";
    private static final String USER_ROLE = "role";

    private final String jwtSecret;
    private final Long validityInMilliseconds;

    public JwtProvider(
            @Value("${roomescape.auth.jwt.secret}") String jwtSecret,
            @Value("${roomescape.auth.jwt.expiration}") Long expireMilliseconds
    ) {
        this.jwtSecret = jwtSecret;
        this.validityInMilliseconds = expireMilliseconds;
    }

    public MemberCredential create(MemberAuthContext context) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String tokenValue = Jwts.builder()
                .claim(USER_NAME, context.name())
                .claim(USER_ROLE, context.role())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret)))
                .compact();
        return new MemberCredential(tokenValue);
    }

    @Override
    public MemberAuthContext parseCredential(MemberCredential token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret)))
                .build()
                .parseClaimsJws(token.authorization())
                .getBody();

        return new MemberAuthContext(
                claims.get(USER_NAME, String.class),
                claims.get(USER_ROLE, String.class)
        );
    }
}
