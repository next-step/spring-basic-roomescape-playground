package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.model.Member;

@Component
public class JwtTokenProvider {
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Value("${roomescape.auth.jwt.expiry}")
    private long expiryInMs;

    public String createToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getId().toString());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiryInMs);

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(claims)
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String extractTokenFromCookies(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter((cookie) -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
