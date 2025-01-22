package roomescape.auth.util;


import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieProvider {

	public Cookie createCookie(String token) {
		Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}

	public String extractTokenFromCookie(Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("token")) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
