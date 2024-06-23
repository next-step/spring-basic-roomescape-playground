package roomescape.jwt;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;

@Component
public class JwtProvider {

	private String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

	public String createToken(Member member) {
		return Jwts.builder()
			.setSubject(member.getId().toString())
			.claim("name", member.getName())
			.claim("role", member.getRole())
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();
	}

	public String createToken(String email, String password) {
		return Jwts.builder()
			.claim("email", email)
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();
	}
}
