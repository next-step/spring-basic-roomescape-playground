package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.security.JwtTokenProvider;
import roomescape.auth.service.AuthService;
import roomescape.member.dao.MemberDao;
import roomescape.member.dto.Member;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private MemberDao memberDao;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("토큰을_올바르게_생성한다.")
    public void testCreateToken() {

        //Given
        String email = "test@test.com";
        Member member = new Member("Test Member", email, "password", "USER");
        String mockToken = "mock-jwt-token";

        // When
        when(memberDao.findByEmail(email)).thenReturn(member);
        when(jwtTokenProvider.createToken(email)).thenReturn(mockToken);

        // Then
        LoginResponse response = authService.createToken(email);
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getAccessToken()).isEqualTo(mockToken)
        );
    }

    @Test
    @DisplayName("유효하지_않은_토큰은_잘못된_응답을_반환한다.")
    public void testIsTokenInvalid() {
        // Given
        String validToken = "valid-jwt-token";
        String invalidToken = "invalid-jwt-token";

        // When
        when(jwtTokenProvider.isTokenInvalid(validToken)).thenReturn(false);
        when(jwtTokenProvider.isTokenInvalid(invalidToken)).thenReturn(true);

        // Then
        assertAll(
                () -> assertThat(authService.isTokenInvalid(validToken)).isFalse(),
                () -> assertThat(authService.isTokenInvalid(invalidToken)).isTrue()
        );
    }
}
