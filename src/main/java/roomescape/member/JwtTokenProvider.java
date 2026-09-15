package roomescape.member;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretKey;

    // 생성자를 통해 application.properties의 값을 주입받도록 수정
    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }
    public String createToken(Member member) {
        // 1) 만료기한 설정을 위해 현재 시간/만료 시간을 계산
        Date now = new Date();
        Date validaity = new Date(now.getTime() + 1800000);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now) // 2) 발급 시간 설정
                .setExpiration(validaity) // 만료 기한(30분) 설정
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes())) // 비밀 키를 해시 알고리즘 형태로 바꿔 서명(위조 방지 목적)
                .compact();
    }

    // 토큰을 해독해 사용자의 ID 꺼내기
    // 추가) 토큰 만료 시 UnauthorizrdException 예외를 던진다
    public Long getMemberId(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
        }
    }

}
