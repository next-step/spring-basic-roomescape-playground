package roomescape.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.service.AuthService;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.auth.util.CookieProvider;

@RestController
public class AuthController {
	private AuthService authService;
	private CookieProvider cookieProvider;

	public AuthController(AuthService authService, CookieProvider cookieProvider) {
		this.authService = authService;
		this.cookieProvider = cookieProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		TokenResponse tokenResponse = authService.login(loginRequest);
		response.addCookie(cookieProvider.createCookie(tokenResponse.token()));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login/check")
	public ResponseEntity<?> checkLogin(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String token = cookieProvider.extractTokenFromCookie(cookies)
			.orElseThrow(() -> new IllegalArgumentException("쿠키에 정보가 존재하지 않습니다."));
		return ResponseEntity.ok().body(authService.checkLoginStatus(token));
	}
}
