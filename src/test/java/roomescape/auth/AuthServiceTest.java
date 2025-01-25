package roomescape.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AuthService authService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("testName", "test@email.com", "testPassword", "user");
        memberDao.save(member);
    }

    @Test
    @DisplayName("이메일 및 비밀번호로 로그인 성공")
    void loginWithEmailAndPassword_Success() {
        String actualToken = authService.loginWithEmailAndPassword(member.getEmail(), member.getPassword());

        assertThat(actualToken).isNotBlank();
    }

    @Test
    @DisplayName("유효하지 않은 이메일과 비밀번호")
    void loginWithEmailAndPassword_Failure_InvalidCredentials() {
        assertThatThrownBy(() -> authService.loginWithEmailAndPassword("invalid_email", "invalid_password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 이메일 또는 비밀번호입니다.");
    }

    @Test
    @DisplayName("토큰으로 로그인 성공")
    void loginCheckWithToken_Success() {
        String token = authService.loginWithEmailAndPassword(member.getEmail(), member.getPassword());

        MemberDetailResponse response = authService.loginCheckWithToken(token);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(member.getName());
        assertThat(response.email()).isEqualTo(member.getEmail());
        assertThat(response.role()).isEqualTo(member.getRole());
    }

    @Test
    @DisplayName("잘못된 토큰으로 로그인시 실패")
    void loginCheckWithToken_Failure_InvalidToken() {
        String token = "invalid_token";

        assertThatThrownBy(() -> authService.loginCheckWithToken(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 토큰입니다.");
    }
}