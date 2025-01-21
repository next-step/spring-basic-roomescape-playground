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
import roomescape.member.Member;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@RestController
public class AuthController {
	private TokenService tokenService;
	private MemberService memberService;

	public AuthController(TokenService tokenService, MemberService memberService) {
		this.tokenService = tokenService;
		this.memberService = memberService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		String email = loginRequest.email();
		String password = loginRequest.password();

		Member member = memberService.findMemberByEmailAndPassword(email, password);
		String token = tokenService.createAccessToken(member);
		response.addCookie(CookieUtil.createCookie(token));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login/check")
	public ResponseEntity<?> checkLogin(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String token = CookieUtil.extractTokenFromCookie(cookies);
		if (token != null) {
			MemberResponse memberResponse = tokenService.extractMemberResponseFromToken(token);
			return ResponseEntity.ok().body(memberResponse);
		}
		return ResponseEntity.notFound().build();
	}
}
