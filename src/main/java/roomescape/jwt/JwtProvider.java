package roomescape.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;

@Component
public class JwtProvider {

	@Value("${roomescape.auth.jwt.secret}")
	private String secretKey;

	public String createToken(Member member) {
		return Jwts.builder()
			.setSubject(member.getId().toString())
			.claim("name", member.getName())
			.claim("role", member.getRole())
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact();
	}
}
