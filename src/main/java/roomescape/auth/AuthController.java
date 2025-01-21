package roomescape.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.util.CookieUtil;

@RestController
public class AuthController {
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		TokenResponse tokenResponse = authService.login(loginRequest);
		response.addCookie(CookieUtil.createCookie(tokenResponse.token()));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login/check")
	public ResponseEntity<?> checkLogin(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String token = CookieUtil.extractTokenFromCookie(cookies);
		return ResponseEntity.ok().body(authService.checkLoginStatus(token));
	}
}
