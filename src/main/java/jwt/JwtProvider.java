package jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.member.Member;
import roomescape.member.Role;

import java.util.Date;

public class JwtProvider {

    private final JwtProperties jwtProperties;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Long extractMemberId(String token) {
        try {
            String subject = parseClaim(token).getSubject();

            if (subject == null) {
                throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "토큰 subject가 누락되어 있습니다.");
            }
            Long memberId = Long.valueOf(subject);
            if (memberId <= 0) {
                throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "유효하지 않은 id입니다.");
            }
            return memberId;

        } catch (NumberFormatException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "토큰 subject가 숫자 형식이 아닙니다.");
        }
    }

    public Role extractRole(String token) {
        Claims claims = parseClaim(token);
        String role = claims.get("role", String.class);

        if (role == null) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "권한 정보가 없습니다");
        }
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN, "유효하지 않은 권한 정보입니다");
        }
    }

    public String createToken(Member member) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("role", member.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                .compact();
        return accessToken;
    }

    private Claims parseClaim(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RoomEscapeException(ErrorCode.EXPIRED_TOKEN);
        } catch (SignatureException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_TOKEN_SIGNATURE);
        } catch (JwtException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_LOGIN, "유효하지 않은 jwt입니다");
        }
    }
}
