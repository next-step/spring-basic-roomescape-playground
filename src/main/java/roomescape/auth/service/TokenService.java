package roomescape.auth.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.auth.dto.response.MemberTokenDto;
import roomescape.common.util.TimeProvider;

@Service
public class TokenService {
	private final SecretKey secretKey;
	private final Long expiration;
	private final TimeProvider timeProvider;

	public TokenService(@Value("${roomescape.auth.jwt.secret.key}") String secretKey,
		@Value("${roomescape.auth.jwt.secret.expiration}") Long expiration, TimeProvider timeProvider) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
		this.expiration = expiration;
		this.timeProvider = timeProvider;
	}

	public TokenResponse createAccessToken(MemberTokenDto memberTokenDto) {
		Date now = timeProvider.now();
		return new TokenResponse(Jwts.builder()
			.setSubject(memberTokenDto.id().toString())
			.claim("name", memberTokenDto.name())
			.claim("role", memberTokenDto.role())
			.setExpiration(createExpiration(now, expiration))
			.signWith(secretKey)
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
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
