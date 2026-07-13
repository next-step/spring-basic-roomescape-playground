package roomescape.auth.jwt;

import io.jsonwebtoken.Claims;
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
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(JwtTokenProperty tokenProperty) {
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(tokenProperty.secretKey()));
        this.accessTokenExpiration = tokenProperty.accessTokenExpiration();
        this.refreshTokenExpiration = tokenProperty.refreshTokenExpiration();
    }

    public String createAccessToken(Member member) {
        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Member member) {
        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .signWith(secretKey)
                .compact();
    }

    public Long getLoginMemberId(String token) {
        return Long.valueOf(
                parseClaims(token).getSubject()
        );
    }

    public String getLoginMemberName(String token) {
        return parseClaims(token).get("name", String.class);
    }

    public String getLoginMemberRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            throw new ApplicationException(AuthErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(AuthErrorCode.UNAUTHENTICATED_ACCESS);
        }
    }
}
