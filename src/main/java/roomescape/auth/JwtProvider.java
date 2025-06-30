package roomescape.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.member.Member;

import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Long extractMemberId(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject());
        } catch (ExpiredJwtException e) {
            throw new RoomEscapeException(ErrorCode.EXPIRED_TOKEN);
        } catch (SignatureException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN_SIGNATURE);
        }
    }

    public String createToken(Member member) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                .compact();
        return accessToken;
    }
}
