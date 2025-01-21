package roomescape.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;
import roomescape.member.MemberResponse;

@Service
public class TokenService {
	
	@Value("${roomescape.auth.jwt.secret}")
	private String secrectKey;

	public String createAccessToken(Member member) {
		return Jwts.builder()
			.setSubject(member.getId().toString())
			.claim("name", member.getName())
			.claim("role", member.getRole())
			.signWith(Keys.hmacShaKeyFor(secrectKey.getBytes()))
			.compact();
	}

	public MemberResponse extractMemberResponseFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secrectKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody();

		return new MemberResponse(Long.valueOf(claims.getSubject()), String.valueOf(claims.get("name")),
			String.valueOf(claims.get("role")));
	}
}
