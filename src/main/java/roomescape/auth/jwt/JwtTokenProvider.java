package roomescape.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;
import roomescape.auth.exception.AuthErrorCode;
import roomescape.exception.ApplicationException;
import roomescape.member.domain.Member;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long expiration;

    public JwtTokenProvider(JwtTokenProperty tokenProperty) {
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(tokenProperty.secretKey()));
        this.expiration = tokenProperty.expiration();
    }

    public String createAccessToken(Member member) {
        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .signWith(secretKey)
                .compact();
    }

    public Long getLoginMemberId(String accessToken) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody().getSubject());
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            throw new ApplicationException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS);
        }
    }
}
