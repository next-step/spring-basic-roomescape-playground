package roomescape.auth.util;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.Cookie;

class CookieUtilTest {
	private String token;
	private CookieProvider cookieProvider;

	@BeforeEach
	void init() {
		token = "testCookieToken";
		cookieProvider = new CookieProvider();
	}

	@Test
	void 쿠키를_생성할_수_있다() {
		Cookie cookie =  cookieProvider.createCookie(token);

		assertThat(cookie.getValue()).isEqualTo(token);
	}

	@Test
	void 쿠키로부터_토큰을_가져올_수_있다() {
		Cookie[] cookies = new Cookie[] {cookieProvider.createCookie(token)};

		String tokenFromCookie = cookieProvider.extractTokenFromCookie(cookies)
				.orElse(null);
		assertThat(tokenFromCookie).isEqualTo(token);
	}
}
