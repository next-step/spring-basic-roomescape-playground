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
            String subject = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject();

            if (subject == null) {
                throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "토큰 subject가 누락되어 있습니다.");
            }
            Long memberId = Long.valueOf(subject);
            if (memberId <= 0) {
                throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "유효하지 않은 id입니다.");
            }
            return memberId;

        } catch (ExpiredJwtException e) {
            throw new RoomEscapeException(ErrorCode.EXPIRED_TOKEN);
        } catch (SignatureException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN_SIGNATURE);
        } catch (NumberFormatException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "토큰 subject가 숫자 형식이 아닙니다.");
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
