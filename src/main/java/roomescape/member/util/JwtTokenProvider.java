package roomescape.member.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.common.exception.BadRequestException;
import roomescape.common.exception.ExceptionMessage;
import roomescape.member.Member;

@Component
public class JwtTokenProvider {

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

    public long parseAccessToken(final String accessToken) {
        try {
            final Claims claims = parseClaims(accessToken);
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException exception) {
            throw new BadRequestException(ExceptionMessage.EXPIRED_TOKEN.getMessage());
        } catch (JwtException exception) {
            throw new BadRequestException(ExceptionMessage.INVALID_TOKEN.getMessage());
        }
    }

    private Claims parseClaims(final String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }
}
