package roomescape.auth.util;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import jakarta.servlet.http.Cookie;

class CookieUtilTest {
	private final String token = "testCookieToken";

	@Test
	void 쿠키를_생성할_수_있다() {
		Cookie cookie = CookieUtil.createCookie(token);

		assertThat(cookie.getValue()).isEqualTo(token);
	}

	@Test
	void 쿠키로부터_토큰을_가져올_수_있다() {
		Cookie[] cookies = new Cookie[] {CookieUtil.createCookie(token)};

		String tokenFromCookie = CookieUtil.extractTokenFromCookie(cookies);
		assertThat(tokenFromCookie).isEqualTo(token);
	}
}
