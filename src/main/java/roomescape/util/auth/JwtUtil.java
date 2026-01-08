package roomescape.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.util.Date;

public final class JwtUtil {
    private JwtUtil() {
    }

	public static final int DEFAULT_MAX_AGE_SECONDS = 10 * 60; // 10분

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
		Date expiresAt = new Date(now.getTime() + (10 * 60 * 1000)); // 10분
		return Jwts.builder()
				.setSubject(subject)
				.setExpiration(expiresAt)
				.claim("name", name)
				.claim("role", role)
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.compact();
	}

	public static Cookie createAuthCookie(String token, int maxAgeSeconds, boolean secure) {
		Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeSeconds);
		cookie.setSecure(secure);
		return cookie;
	}

	public static Cookie createExpiredAuthCookie() {
		Cookie cookie = new Cookie("token", "");
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		return cookie;
	}
}


