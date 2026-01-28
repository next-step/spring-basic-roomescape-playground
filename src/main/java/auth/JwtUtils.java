package auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

import java.util.Date;

public final class JwtUtils {
    public static final int DEFAULT_MAX_AGE_SECONDS = 10 * 60; // 10 minutes

    private final String secretKey;

    public JwtUtils(String secretKey) {
        this.secretKey = secretKey;
    }

    public String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createToken(String subject, String name, String role) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + (long) DEFAULT_MAX_AGE_SECONDS * 1000);
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiresAt)
                .claim("name", name)
                .claim("role", role)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}


