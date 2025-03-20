package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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
}
