package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        Member member = memberService.login(memberRequest.getEmail(), memberRequest.getPassword());

        String accessToken = createToken(member);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
	public ResponseEntity<MemberResponseDto> checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());

        String name = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("name", String.class);

		MemberResponseDto body = new MemberResponseDto(null, name, null);
        return ResponseEntity.ok(body);
    }

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
    

    public String createTokenFromEmailAndPassword(String email, String password) {
        Member member = memberService.login(email, password);
        return createToken(member);
    }


    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
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
