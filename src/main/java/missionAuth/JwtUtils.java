package missionAuth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Role;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private final Key key;
    private final long validityInMilliseconds;

    public JwtUtils(String secretKey, long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(Long memberId, String name, Role role) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .setExpiration(expire)
                .claim("name", name)
                .claim("role", role.name())
                .signWith(key)
                .compact();
    }

    public JwtDto parse(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long id = Long.valueOf(claims.getSubject());
        String name = claims.get("name", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));

        return new JwtDto(id, name, role);

    }
}
