package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

@Component
public class JwtTokenManager {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim(AuthConfig.NAME.getValue(), member.getName())
                .claim(AuthConfig.ROLE.getValue(), member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String getValueFromJwtToken(final String token, final AuthConfig key) {
        // 해당 토큰에서 인증 정보 조회
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(
                        secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 토큰에 담긴 "key"를 추출
        return claims.get(key.getValue(), String.class);
    }

    public Long getTokenPrincipal(final String token) {
        // Principal은 토큰의 사용자 ID를 의미
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(
                        secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
