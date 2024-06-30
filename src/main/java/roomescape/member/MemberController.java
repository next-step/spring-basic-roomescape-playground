package roomescape.member;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.jwt.JwtUtil;

@RestController
public class MemberController {
	private MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/members")
	public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
		MemberResponse member = memberService.createMember(memberRequest);
		return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
	}

	@PostMapping("/login")
	public void login(@RequestBody MemberLoginRequest request, HttpServletResponse response) {
		String token = memberService.login(request);
		Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	@GetMapping("/login/check")
	public MemberCheckResponse checkLogin(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String token = JwtUtil.extractTokenFromCookie(cookies);
		return memberService.checkMember(token);
	}

	@PostMapping("/logout")
	public ResponseEntity logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("token", "");
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return ResponseEntity.ok().build();
	}
}
