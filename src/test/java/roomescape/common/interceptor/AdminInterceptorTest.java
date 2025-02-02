package roomescape.common.interceptor;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@Transactional
public class AdminInterceptorTest {

	@Autowired
	private AuthService authService;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private AdminInterceptor adminInterceptor;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		memberDao.save(new Member("test", "test@email.com", "password", "USER"));
	}

	@Test
	void 관리자로_로그인할_수_있다() throws Exception {
		String token = authService.login(new LoginRequest("admin@email.com", "password")).token();
		Cookie cookie = new Cookie("token", token);
		request.setCookies(cookie);

		boolean result = adminInterceptor.preHandle(request, response, null);

		assertThat(result).isTrue();
		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
	}

	@Test
	void 토큰이_없는_경우_400_에러가_발생한다() throws Exception {
		Cookie cookie = new Cookie("invalidToken", "invalidToken");
		request.setCookies(cookie);

		boolean result = adminInterceptor.preHandle(request, response, null);

		assertThat(result).isFalse();
		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Test
	void 관리자_권한이_아닌_경우_401_에러가_발생한다() throws Exception {
		String token = authService.login(new LoginRequest("test@email.com", "password")).token();
		Cookie cookie = new Cookie("token", token);
		request.setCookies(cookie);

		boolean result = adminInterceptor.preHandle(request, response, null);

		assertThat(result).isFalse();
		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
