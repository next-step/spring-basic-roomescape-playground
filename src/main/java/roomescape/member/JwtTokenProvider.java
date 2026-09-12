package roomescape.member;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;

import java.util.Date;

@Component
public class JwtTokenProvider {
    // 토큰을 암호화 하고 복호화 할 때 사용하는 비밀 키
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(Member member) {
        // 1) 만료기한 설정을 위해 현재 시간/만료 시간을 계산
        Date now = new Date();
        Date validaity = new Date(now.getTime() + 1800000);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .setIssuedAt(now) // 2) 발급 시간 설정
                .setExpiration(validaity) // 만료 기한(30분) 설정
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 비밀 키를 해시 알고리즘 형태로 바꿔 서명(위조 방지 목적)
                .compact();
    }

    // 토큰을 해독해 사용자의 ID 꺼내기
    // 추가) 토큰 만료 시 UnauthorizrdException 예외를 던진다
    public Long getMemberId(String token) {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
        }
    }

}
