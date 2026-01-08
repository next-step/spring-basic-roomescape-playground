package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.util.JwtUtil;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;
    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
	public ResponseEntity createMember(@RequestBody MemberRequestDto memberRequest) {
		MemberResponseDto member = memberService.createMember(memberRequest);
		return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
	public ResponseEntity login(@RequestBody MemberRequestDto memberRequest, HttpServletResponse response) {
		Member member = memberService.login(memberRequest.email(), memberRequest.password());

		String accessToken = createToken(member);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
	public ResponseEntity<MemberResponseDto> checkLogin(HttpServletRequest request) {
		String token = JwtUtil.extractTokenFromCookies(request.getCookies());

		String name = JwtUtil.parseClaims(token, secretKey).get("name", String.class);

		MemberResponseDto body = new MemberResponseDto(null, name, null);
        return ResponseEntity.ok(body);
    }

    public String createToken(Member member) {
		return JwtUtil.createToken(member.getId().toString(), member.getName(), member.getRole(), secretKey);
    }
    

    public String createTokenFromEmailAndPassword(String email, String password) {
        Member member = memberService.login(email, password);
		return JwtUtil.createToken(member.getId().toString(), member.getName(), member.getRole(), secretKey);
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
