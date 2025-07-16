package roomescape.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jwt.JwtProperties;
import jwt.JwtProvider;
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
    void 잘못된_서명이면_예외_발생() {
        //given
        String badKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E?";
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

    @Test
    @DisplayName("토큰의 subect가 잘못된 형식이면 예외를 던진다")
    void 토큰의_subect가_잘못된_형식이면_예외를_던진다() {
        //given
        String badKey = jwtProperties.getSecret();
        String token = Jwts.builder()
                .setSubject("abc")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(badKey.getBytes()))
                .compact();

        //then
        assertThatThrownBy(() -> jwtProvider.extractMemberId(token))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("토큰 subject가 숫자 형식이 아닙니다.");
    }

    @Test
    @DisplayName("member id의 값이 유효하지 않으면 예외를 던진다")
    void member_id의_값이_유효하지_않으면_예외를_던진다() {
        //given
        String badKey = jwtProperties.getSecret();
        String token = Jwts.builder()
                .setSubject("0")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(badKey.getBytes()))
                .compact();

        //then
        assertThatThrownBy(() -> jwtProvider.extractMemberId(token))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("유효하지 않은 id입니다.");
    }

    @Test
    @DisplayName("subject가_null이면_예외를_던진다")
    void subject가_null이면_예외를_던진다() {
        //given
        String badKey = jwtProperties.getSecret();
        String token = Jwts.builder()
                .setSubject(null)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(badKey.getBytes()))
                .compact();

        //then
        assertThatThrownBy(() -> jwtProvider.extractMemberId(token))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("토큰 subject가 누락되어 있습니다.");
    }

    @Test
    @DisplayName("권한정보가 null이면 예외를 던진다")
    void 권한정보가_null이면_예외를_던진다() {
        //given
        String secret = jwtProperties.getSecret();
        String token = Jwts.builder()
                .setSubject("1")
                .claim("role", null)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

        //then
        assertThatThrownBy(() -> jwtProvider.extractRole(token))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("권한 정보가 없습니다");
    }

    @Test
    @DisplayName("유효하지않은 권한정보이면 예외를 던진다")
    void 유효하지않은_권한정보이면_예외를_던진다() {
        //given
        String secret = jwtProperties.getSecret();
        String token = Jwts.builder()
                .setSubject("1")
                .claim("role", "INVALID_ROLE")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

        //then
        assertThatThrownBy(() -> jwtProvider.extractRole(token))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("유효하지 않은 권한 정보입니다");
    }
}
