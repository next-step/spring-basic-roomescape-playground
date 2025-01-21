package roomescape.auth.util;


import jakarta.servlet.http.Cookie;


public class CookieUtil {

	public static Cookie createCookie(String token) {
		Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}

	public static String extractTokenFromCookie(Cookie[] cookies) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("token")) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
