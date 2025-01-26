package roomescape.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.AuthService;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminInterceptorTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberDao memberDao;

    private AdminInterceptor adminInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        adminInterceptor = new AdminInterceptor(authService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        memberDao.save(new Member("testName", "test@email.com", "testPassword", "USER"));
    }

    @Test
    @DisplayName("관리자로 로그인 시 성공")
    void adminCheck_Success() throws Exception {
        String token = authService.loginWithEmailAndPassword("admin@email.com", "password");
        Cookie cookie = new Cookie("token", token);
        request.setCookies(cookie);

        boolean result = adminInterceptor.preHandle(request, response, null);

        assertThat(result).isTrue();
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("토큰이 없는 경우 400 반환")
    void adminCheck_Failure_NoCookies() throws Exception {
        Cookie cookie = new Cookie("invalid_token_name", "invalid_token");
        request.setCookies(cookie);

        boolean result = adminInterceptor.preHandle(request, response, null);

        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("권한이 없는 경우 401 반한")
    void adminCheck_Failure_Unauthorized() throws Exception {
        String token = authService.loginWithEmailAndPassword("test@email.com", "testPassword");
        Cookie cookie = new Cookie("token", token);
        request.setCookies(cookie);

        boolean result = adminInterceptor.preHandle(request, response, null);
        assertThat(result).isFalse();
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
    }

}