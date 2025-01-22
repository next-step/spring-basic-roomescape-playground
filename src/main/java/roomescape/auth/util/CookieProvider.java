package roomescape.auth.util;


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

	public String extractTokenFromCookie(Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TOKEN)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
