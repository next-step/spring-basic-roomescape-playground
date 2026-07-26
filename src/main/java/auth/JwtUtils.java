package auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import roomescape.member.Member;

import java.security.Key;
import java.util.Date;


public class JwtUtils {
    private final Key secretKey;
    private final long expireAccessTime = 30L * 60 * 1000;
    private final long expireRefreshTime = 14L * 24 * 60 * 60 * 1000;

    public JwtUtils(Key secretKey) {
        this.secretKey = secretKey;
    }

    public String createAccessToken(Member member) {
        return createToken(member.getId(),expireAccessTime);
    }

    public String createRefreshToken(Member member) {
        return createToken(member.getId(),expireRefreshTime);
    }
    private String createToken(Long memberId, long expireTime){
        Date now= new Date();

        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+expireTime))
                .signWith(secretKey)
                .compact();
    }

    public Long getMemberId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다.", e);
        }
    }
}
