package roomescape.auth.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    private String secretKey;
    private long expiration;

    @BeforeEach
    void setUp() {
        secretKey = "ThisIsSecretKeyForTokenServiceTest";
        expiration = 180000L;
        tokenService = new TokenService(secretKey, expiration);
    }

    @Test
    @DisplayName("토큰 생성 성공")
    void createToken_Success() {
        //given
        MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "testName", "email@test.com", "USER");

        //when
        String actualToken = tokenService.createToken(memberTokenDto);
        MemberTokenDto actualClaims = tokenService.getMemberClaims(actualToken);

        //then
        assertThat(actualToken).isNotEmpty();
        assertThat(actualClaims.id()).isEqualTo(memberTokenDto.id());
        assertThat(actualClaims.name()).isEqualTo(memberTokenDto.name());
        assertThat(actualClaims.email()).isEqualTo(memberTokenDto.email());
        assertThat(actualClaims.role()).isEqualTo(memberTokenDto.role());

    }

    @Test
    @DisplayName("토큰 검증 성공")
    void checkValidToken_Success() {
        //given
        MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "testName", "email@test.com", "USER");
        String token = tokenService.createToken(memberTokenDto);

        //when
        boolean isValid = tokenService.checkValidToken(token);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰 전달시 검증")
    void checkValidToken_Failure_ExpiredToken() throws InterruptedException {
        //given
        TokenService zeroExpirationTokenService = new TokenService(secretKey, 0);
        MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "testName", "email@test.com", "USER");
        String token = zeroExpirationTokenService.createToken(memberTokenDto);

        //when
        Thread.sleep(1000);
        boolean isValid = zeroExpirationTokenService.checkValidToken(token);

        //then
        assertThat(isValid).isFalse();
    }


    @Test
    @DisplayName("토큰 해석 성공")
    void getMemberClaims_Success() {
        //given
        MemberTokenDto memberTokenDto = new MemberTokenDto(1L, "testName", "email@test.com", "USER");
        String token = tokenService.createToken(memberTokenDto);

        //when
        MemberTokenDto actualClaims = tokenService.getMemberClaims(token);

        //then
        assertThat(actualClaims).isNotNull();
        assertThat(actualClaims.id()).isEqualTo(memberTokenDto.id());
        assertThat(actualClaims.name()).isEqualTo(memberTokenDto.name());
        assertThat(actualClaims.email()).isEqualTo(memberTokenDto.email());
        assertThat(actualClaims.role()).isEqualTo(memberTokenDto.role());
    }

    @Test
    @DisplayName("잘못된 토큰 전달시 해석 실패")
    void getMemberClaims_Failure_InvalidToken() {
        //given
        String token = "InvalidToken";

        //when
        assertThatThrownBy(() -> tokenService.getMemberClaims(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 토큰입니다.");
    }
}