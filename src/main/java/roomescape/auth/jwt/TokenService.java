package roomescape.auth.jwt;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import roomescape.auth.TokenResponse;

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

	public boolean validToken(String token) {
		try {
			getClaimsFromToken(token);
			return true;
		} catch (ExpiredJwtException e) {
			throw new IllegalArgumentException("만료된 토큰입니다.");
		} catch (MalformedJwtException e) {
			throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
		} catch (SignatureException e) {
			throw new IllegalArgumentException("조작된 토큰입니다.");
		}
	}

	public MemberTokenDto extractMemberResponseFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return new MemberTokenDto(
			Long.valueOf(claims.getSubject()),
			String.valueOf(claims.get("name")),
			String.valueOf(claims.get("role"))
		);
	}

	private Date createExpiration(Date now, Long expiration) {
		return new Date(now.getTime() + expiration);
	}

	private Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
