package roomescape.auth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import roomescape.auth.jwt.MemberTokenDto;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@SpringBootTest
@TestPropertySource(properties = {
	"roomescape.auth.jwt.secret.key=thisistestkeythisistestkeythisistestkeythisistestkeythisistestkey",
	"roomescape.auth.jwt.secret.expiration=1000"
})
@Transactional
class AuthServiceTest {

	@Autowired
	private AuthService authService;

	@Autowired
	private MemberDao memberDao;

	private String email = "test@test.com";
	private String password = "test123";
	private String name = "testUser";
	private String role = "USER";

	@BeforeEach
	void setUp() {
		Member member = new Member(name, email, password, role);
		memberDao.save(member);
	}

	@Test
	void 로그인을_할_수_있다() {
		LoginRequest loginRequest = new LoginRequest(email, password);

		TokenResponse tokenResponse = authService.login(loginRequest);

		assertThat(tokenResponse.token()).isNotEmpty();
	}

	@Test
	void 토큰에서_사용자_정보를_가져올_수_있다() {
		LoginRequest loginRequest = new LoginRequest(email, password);
		TokenResponse tokenResponse = authService.login(loginRequest);

		MemberTokenDto memberTokenDto = authService.checkLoginStatus(tokenResponse.token());

		assertThat(memberTokenDto.name()).isEqualTo(name);
		assertThat(memberTokenDto.role()).isEqualTo(role);
	}

}