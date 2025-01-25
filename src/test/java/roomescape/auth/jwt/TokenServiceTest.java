package roomescape.auth.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;
    private MemberTokenDto memberTokenDto;

    @BeforeEach
    void setUp() {
        memberTokenDto = new MemberTokenDto(1L, "testName", "test@email.com", "user");
    }

    @Test
    @DisplayName("토큰 생성 성공")
    void createToken_Success() {
        String actualToken = tokenService.createToken(memberTokenDto);
        MemberTokenDto actualClaims = tokenService.getMemberClaims(actualToken);

        assertThat(actualToken).isNotEmpty();
        assertThat(actualClaims.id()).isEqualTo(memberTokenDto.id());
        assertThat(actualClaims.name()).isEqualTo(memberTokenDto.name());
        assertThat(actualClaims.email()).isEqualTo(memberTokenDto.email());
        assertThat(actualClaims.role()).isEqualTo(memberTokenDto.role());

    }

    @Test
    @DisplayName("토큰 검증 성공")
    void checkValidToken_Success() {
        String token = tokenService.createToken(memberTokenDto);

        boolean isValid = tokenService.checkValidToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰 전달시 검증")
    void checkValidToken_Failure_ExpiredToken() throws InterruptedException {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setExpiration(0L);
        jwtConfig.setSecret("ThisIsSecretKeyForTokenServiceTest");

        TokenService zeroExpirationTokenService = new TokenService(jwtConfig);
        String token = zeroExpirationTokenService.createToken(memberTokenDto);

        boolean isValid = zeroExpirationTokenService.checkValidToken(token);

        assertThat(isValid).isFalse();
    }


    @Test
    @DisplayName("토큰 해석 성공")
    void getMemberClaims_Success() {
        String token = tokenService.createToken(memberTokenDto);

        MemberTokenDto actualClaims = tokenService.getMemberClaims(token);

        assertThat(actualClaims).isNotNull();
        assertThat(actualClaims.id()).isEqualTo(memberTokenDto.id());
        assertThat(actualClaims.name()).isEqualTo(memberTokenDto.name());
        assertThat(actualClaims.email()).isEqualTo(memberTokenDto.email());
        assertThat(actualClaims.role()).isEqualTo(memberTokenDto.role());
    }

    @Test
    @DisplayName("잘못된 토큰 전달시 해석 실패")
    void getMemberClaims_Failure_InvalidToken() {
        String token = "InvalidToken";

        assertThatThrownBy(() -> tokenService.getMemberClaims(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 토큰입니다.");
    }
}