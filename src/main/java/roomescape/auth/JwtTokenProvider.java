package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Role;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final Key secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") String rawSecretKey,
            @Value("${security.jwt.token.expire-length}") Long validityInMilliseconds
    ) {
        final byte[] keyBytes = Base64.getDecoder().decode(rawSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String id, String name, String email, Role role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        JwtBuilder builder = Jwts.builder()
                .setSubject(id)
                .claim("name", name)
                .claim("email", email)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey);

        return builder.compact();
    }

    public LoginMember extractLoginMember(String token) {
        Claims claims = parseClaims(token).getBody();
        String name = claims.get("name", String.class);
        String email = claims.get("email", String.class);
        if (name == null || name.isBlank()) {
            log.warn("JWT validation failed: missing name claim");
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        return new LoginMember(
                extractId(claims),
                name,
                email,
                extractRole(claims)
        );
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT parsing failed: {}", e.getMessage());
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }

    private Long extractId(Claims claims) {
        try {
            return Long.valueOf(claims.getSubject());
        } catch (NumberFormatException e) {
            log.warn("JWT validation failed: invalid subject claim");
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }

    private Role extractRole(Claims claims) {
        try {
            return Role.valueOf(claims.get("role", String.class));
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("JWT validation failed: invalid role claim");
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
