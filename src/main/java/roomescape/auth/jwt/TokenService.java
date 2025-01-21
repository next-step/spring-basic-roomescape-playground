package roomescape.auth.jwt;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.auth.TokenResponse;
import roomescape.member.MemberResponse;

@Service
public class TokenService {

	@Value("${roomescape.auth.jwt.secret.key}")
	private String secretKey;

	@Value("${roomescape.auth.jwt.secret.expiration}")
	private Long expiration;

	public TokenResponse createAccessToken(MemberTokenDto memberTokenDto) {
		Date now = new Date();
		return new TokenResponse(Jwts.builder()
			.setSubject(memberTokenDto.id().toString())
			.claim("name", memberTokenDto.name())
			.claim("role", memberTokenDto.role())
			.setExpiration(createExpiration(now, expiration))
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.compact());
	}

	public MemberResponse extractMemberResponseFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody();

		return new MemberResponse(Long.valueOf(claims.getSubject()), String.valueOf(claims.get("name")),
			String.valueOf(claims.get("role")));
	}

	private Date createExpiration(Date now, Long expiration) {
		return new Date(now.getTime() + expiration);
	}
}
