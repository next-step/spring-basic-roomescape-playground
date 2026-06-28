package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.net.URI;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import roomescape.ApiException;
import roomescape.RedirectException;
import roomescape.member.Member;

@Component
public class AuthTokenProvider {
    private static final long TOKEN_VALIDITY_MILLISECONDS = 1000 * 60 * 60 * 24 * 7;

    private final Key secretKey;
    private final JwtParser jwtParser;

    public AuthTokenProvider(@Value("${roomescape.auth.jwt.secret}") String rawSecretKey) {
        byte[] secretKeyBytes = Base64.getDecoder().decode(rawSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    public AuthToken generateSessionToken(AuthorizedMember claims) {
        String token = Jwts.builder()
                .setClaims(generateClaimsForMember(claims))
                .signWith(secretKey)
                .compact();

        return new AuthToken(token);
    }

    private Claims generateClaimsForMember(AuthorizedMember member) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.setIssuedAt(now);
        claims.setExpiration(new Date(now.getTime() + TOKEN_VALIDITY_MILLISECONDS));

        claims.put("name", member.name());
        claims.put("email", member.email());
        claims.put("roles", member.role());
        return claims;
    }

    public AuthorizedMember parseSessionToken(AuthToken token) {
        try {
            Jws<Claims> jws = jwtParser.parseClaimsJws(token.token());
            return getMemberFromToken(jws.getBody());
        } catch (ExpiredJwtException ignored) {
            throw new RedirectException(HttpStatus.TEMPORARY_REDIRECT, URI.create("/login"));
        } catch (JwtException ignored) {
            throw ApiException.status(HttpStatus.UNAUTHORIZED);
        }
    }

    private AuthorizedMember getMemberFromToken(Claims tokenClaims) {
        return new AuthorizedMember(
                tokenClaims.get("name", String.class),
                tokenClaims.get("email", String.class),
                Member.Role.valueOf(tokenClaims.get("roles", String.class))
        );
    }
}
