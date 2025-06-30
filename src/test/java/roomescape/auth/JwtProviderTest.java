package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.exception.RoomEscapeException;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtProperties jwtProperties;

    @Test
    @DisplayName("만료된 토큰이면 예외 발생")
    void 만료된_토큰이면_예외_발생() throws InterruptedException {
        //given
        String token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                .compact();

        Thread.sleep(150);

        //then
        assertThatThrownBy(() -> jwtProvider.extractMemberId(token))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("토큰이 만료되었습니다.");
    }

    @Test
    @DisplayName("잘못된 서명이면 예외 발생")
    void 잘못된_서명이면_예외_발생() throws InterruptedException {
        //given
        String badKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E?";
        System.out.println("dddddd"+ badKey.getBytes().length);
        String token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 100))
                .signWith(Keys.hmacShaKeyFor(badKey.getBytes()))
                .compact();

        //then
        assertThatThrownBy(() -> jwtProvider.extractMemberId(token))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("토큰의 서명이 유효하지 않습니다.");
    }

}
