package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import roomescape.member.Member;

class JwtTokenProviderTest {
    private final String originSecretKey = "ThisIsATestKeyForJsonWebTokenProvider";
    private final long originValidity = 6000;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            new JwtProperties(originSecretKey, originValidity));
    private final Member member = new Member(1L, "test", "test@email.com", "ADMIN");

    @Test
    void 토큰_생성_성공() {
        //when
        String token = jwtTokenProvider.createToken(member);

        //then
        assertThat(token).isNotBlank();
    }

    @Test
    void 토큰_정보_조회_성공() {
        //given
        String token = jwtTokenProvider.createToken(member);

        //when
        Map<String, Object> claims = jwtTokenProvider.getClaims(token);

        //then
        assertThat(claims.get("sub")).isEqualTo(member.getEmail());
        assertThat(claims.get("name")).isEqualTo(member.getName());
        assertThat(claims.get("role")).isEqualTo(member.getRole());
    }

    @Test
    void 토큰이_만료된_경우_토큰_정보_조회에_실패한다() {
        //given
        JwtTokenProvider otherProvider = new JwtTokenProvider(new JwtProperties(originSecretKey, 0));
        String expireToken = otherProvider.createToken(member);

        //when, then
        assertThatThrownBy(() -> jwtTokenProvider.getClaims(expireToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void 토큰의_서명이_다른_경우_토큰_정보_조회에_실패한다() {
        //given
        JwtTokenProvider otherProvider = new JwtTokenProvider(
                new JwtProperties(originSecretKey + " ", originValidity));
        String alteredSignatureToken = otherProvider.createToken(member);

        //when, then
        assertThatThrownBy(() -> jwtTokenProvider.getClaims(alteredSignatureToken))
                .isInstanceOf(SignatureException.class);
    }
}
