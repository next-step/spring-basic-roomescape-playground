package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "roomescape-secret-key-for-jwt-token";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("id", member.getId())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public LoginMemberInfo parseMember(String token) {
        try {
            Claims claims = parseClaims(token);
            Long id = claims.get("id", Number.class).longValue();
            String name = claims.get("name").toString();
            String email = claims.getSubject();
            String role = claims.get("role").toString();
            return new LoginMemberInfo(id, name, email, role);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private Claims parseClaims(String token) {
        if (!isValidSignature(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        return jwtParser()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isValidSignature(String token) {
        try {
            jwtParser().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private JwtParser jwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build();
    }
}
