package roomescape.jwt;

import java.util.Arrays;

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
		if (cookies == null) {
			throw new IllegalArgumentException("Cookies array is null");
		}

		return Arrays.stream(cookies)
			.filter(cookie -> "token".equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Token cookie not found"));
	}

	public static Long getIdFromToken(String token) {
		return Long.valueOf(Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody().getSubject());
	}
}
