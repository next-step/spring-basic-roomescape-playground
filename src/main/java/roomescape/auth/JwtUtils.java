package roomescape.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import roomescape.member.Member;

public class JwtUtils {

	private String secretKey;

	@Value("${roomescape.auth.jwt.secret}")
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String createToken(Member member) {
		return Jwts.builder()
			.setSubject(member.getId().toString())
			.claim("name", member.getName())
			.claim("role", member.getRole())
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();
	}

	public String extractTokenFromCookie(Cookie[] cookies) {
		if (cookies == null) {
			throw new IllegalArgumentException("Cookies array is null");
		}

		return Arrays.stream(cookies)
			.filter(cookie -> "token".equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Token cookie not found"));
	}

	public Long getIdFromToken(String token) {
		return Long.valueOf(Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody().getSubject());
	}
}
