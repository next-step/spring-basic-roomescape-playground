package roomescape.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.util.Date;

public final class JwtUtil {
    private JwtUtil() {
    }

    public static String extractTokenFromCookies(Cookie[] cookies) {
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

    public static Claims parseClaims(String token, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

	public static String createToken(String subject, String name, String role, String secretKey) {
		Date now = new Date();
		Date expiresAt = new Date(now.getTime() + (10 * 60 * 1000)); // 10 minutes
		return Jwts.builder()
				.setSubject(subject)
				.setExpiration(expiresAt)
				.claim("name", name)
				.claim("role", role)
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.compact();
	}
}


