package roomescape.auth.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import roomescape.common.util.TestTimeProvider;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.MemberTokenDto;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.common.util.TimeProvider;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.member.service.MemberService;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@Transactional
class AuthServiceTest {

	private AuthService authService;
	private TimeProvider timeProvider;

	@Autowired
	private MemberDao memberDao;

	private String email = "test@test.com";
	private String password = "test123";
	private String name = "testUser";
	private String role = "USER";

	@BeforeEach
	void setUp() {
		timeProvider = new TestTimeProvider(new Date());
		TokenService tokenService = new TokenService("thisistestkeythisistestkeythisistestkeythisistestkeythisistestkey", 1000L, timeProvider);
		authService = new AuthService(tokenService, new MemberService(memberDao));
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