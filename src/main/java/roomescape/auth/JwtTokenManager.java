package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.exception.UnAuthorizedException;
import roomescape.member.domain.Member;

import java.util.Date;

@Component
public class JwtTokenManager {

    public static final long ACCESS_TOKEN_EXP = 60L * 60L * 1000L; // 1시간
    private static final int BITS_IN_BYTE = 8;
    private static final int MIN_KEY_SIZE = 256;

    private final String secretKey;

    public JwtTokenManager(@Value("${roomescape.auth.jwt.secret}") String secretKey) {
        validateSecretKey(secretKey);
        this.secretKey = secretKey;
    }

    private void validateSecretKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        int keySizeInBits = keyBytes.length * BITS_IN_BYTE;

        if (keySizeInBits < MIN_KEY_SIZE) {
            throw new BadRequestException(ExceptionMessage.INVALID_SECRET_KEY.getMessage());
        }
    }

    public String createAccessToken(Member member) {
        return createToken(member, ACCESS_TOKEN_EXP);
    }

    private String createToken(Member member, long expirationTime) {
        Date expirationDate = calculateExpirationDateFromNow(expirationTime);
        try {
            return Jwts.builder()
                    .setSubject(member.getId().toString())
                    .claim("name", member.getName())
                    .claim("role", member.getRole())
                    .setExpiration(expirationDate)
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();
        } catch (WeakKeyException weakKeyException) {
            throw new BadRequestException(ExceptionMessage.INVALID_SECRET_KEY.getMessage());
        }
    }

    private Date calculateExpirationDateFromNow(long expirationTime) {
        long currentDateTime = new Date().getTime();
        return new Date(currentDateTime + expirationTime);
    }

    public long parseToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException exception) {
            throw new UnAuthorizedException(ExceptionMessage.EXPIRED_TOKEN.getMessage());
        } catch (JwtException exception) {
            throw new UnAuthorizedException(ExceptionMessage.INVALID_TOKEN.getMessage());
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getSecretKey() {
        return secretKey;
    }
}
