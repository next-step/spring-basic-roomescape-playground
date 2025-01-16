package roomescape.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@RestController
public class AuthController {
	private MemberDao memberDao;
	private TokenService tokenService;
	private CookieService cookieService;

	public AuthController(MemberDao memberDao, TokenService tokenService, CookieService cookieService) {
		this.memberDao = memberDao;
		this.tokenService = tokenService;
		this.cookieService = cookieService;
	}

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		Member member = memberDao.findByEmailAndPassword(email, password);
		String token = tokenService.createAccessToken(member);
		response.addCookie(cookieService.createCookie(token));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login/check")
	public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String token = cookieService.extractTokenFromCookie(cookies);
		MemberResponse memberResponse = tokenService.extractMemberResponseFromToken(token);
		return ResponseEntity.ok().body(memberResponse);
	}
}
