package roomescape.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.auth.jwt.MemberTokenDto;
import roomescape.auth.jwt.TokenService;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private MemberDao memberDao;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Member member;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "testName";
        email = "email@test.com";
        password = "testPassword";
        member = new Member(id, name, email, role);
    }

    @Test
    @DisplayName("이메일 및 비밀번호로 로그인 성공")
    void loginWithEmailAndPassword_Success() {
        //given
        String expectedToken = "test_token";

        when(memberDao.findByEmailAndPassword(email, password)).thenReturn(member);
        when(tokenService.createToken(any(MemberTokenDto.class))).thenReturn(expectedToken);

        //when
        String actualToken = authService.loginWithEmailAndPassword(email, password);

        //then
        assertThat(actualToken).isEqualTo(expectedToken);
        verify(memberDao, times(1)).findByEmailAndPassword(email, password);
        verify(tokenService, times(1)).createToken(any(MemberTokenDto.class));
    }

    @Test
    @DisplayName("유효하지 않은 이메일과 비밀번호")
    void loginWithEmailAndPassword_Failure_InvalidCredentials() {
        //given
        String expectedToken = "test_token";

        when(memberDao.findByEmailAndPassword(email, password)).thenReturn(null);

        //when, then
        assertThatThrownBy(() -> authService.loginWithEmailAndPassword(email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 이메일 또는 비밀번호입니다.");
        verify(memberDao, times(1)).findByEmailAndPassword(email, password);
        verify(tokenService, never()).createToken(any(MemberTokenDto.class));
    }

    @Test
    @DisplayName("토큰으로 로그인 성공")
    void loginCheckWithToken_Success() {
        //given
        String token = "test_token";
        MemberTokenDto memberTokenDto = new MemberTokenDto(id, name, email, role);

        when(tokenService.checkValidToken(token)).thenReturn(true);
        when(tokenService.getMemberClaims(token)).thenReturn(memberTokenDto);

        //when
        MemberDetailResponse response = authService.loginCheckWithToken(token);

        //then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(memberTokenDto.id());
        assertThat(response.name()).isEqualTo(memberTokenDto.name());
        assertThat(response.email()).isEqualTo(memberTokenDto.email());
        assertThat(response.role()).isEqualTo(memberTokenDto.role());
        verify(tokenService, times(1)).checkValidToken(token);
        verify(tokenService, times(1)).getMemberClaims(token);
    }

    @Test
    @DisplayName("잘못된 토큰으로 로그인시 실패")
    void loginCheckWithToken_Failure_InvalidToken() {
        //given
        String token = "invalid_token";

        when(tokenService.checkValidToken(token)).thenReturn(false);

        //when, then
        assertThatThrownBy(() -> authService.loginCheckWithToken(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 토큰입니다.");
        verify(tokenService, times(1)).checkValidToken(token);
        verify(tokenService, never()).getMemberClaims(token);
    }
}