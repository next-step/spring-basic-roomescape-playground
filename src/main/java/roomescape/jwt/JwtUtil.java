package roomescape.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

public class JwtUtil {

	public static String extractTokenFromCookie(Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("token")) {
				return cookie.getValue();
			}
		}
		return "";
	}

	public static Long decodeToken(String token) {
		return Long.valueOf(Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody().getSubject());
	}

}
