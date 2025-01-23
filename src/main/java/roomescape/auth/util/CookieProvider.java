package roomescape.auth.util;


import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieProvider {
	private final String TOKEN = "token";

	public Cookie createCookie(String token) {
		Cookie cookie = new Cookie(TOKEN, token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}

	public Optional<String> extractTokenFromCookie(Cookie[] cookies) {
		return Optional.ofNullable(cookies)
			.flatMap(cks -> Arrays.stream(cks)
				.filter(cookie -> cookie.getName().equals(TOKEN))
				.map(Cookie::getValue)
				.findFirst());
	}
}
