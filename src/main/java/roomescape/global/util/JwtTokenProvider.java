package roomescape.global.util;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import roomescape.member.domain.Member;

import java.security.Key;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getExpireLength());

        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getMemberId(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
