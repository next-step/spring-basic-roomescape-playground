package roomescape.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

@Component
public class JwtUtil {

	private static String secretKey;

	@Value("${roomescape.auth.jwt.secret}")
	public void setSecretKey(String secretKey) {
		JwtUtil.secretKey = secretKey;
	}

	public static String extractTokenFromCookie(Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("token")) {
				return cookie.getValue();
			}
		}
		return "";
	}

	public static Long getIdFromToken(String token) {
		return Long.valueOf(Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody().getSubject());
	}
}
