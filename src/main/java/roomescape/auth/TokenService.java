package roomescape.auth;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.member.Member;

@Service
public class TokenService {
	private	final String secrectKey = "EwordIsMyBestTeacherEwordIsMyBestTeacherEwordIsMyBestTeacherEwordIsMyBestTeacherEwordIsMyBestTeacher";

	public String createAccessToken(Member member) {
		return Jwts.builder()
			.setSubject(member.getId().toString())
			.claim("name", member.getName())
			.claim("role", member.getRole())
			.signWith(Keys.hmacShaKeyFor(secrectKey.getBytes()))
			.compact();
	}
}
