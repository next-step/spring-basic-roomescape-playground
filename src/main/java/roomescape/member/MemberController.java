package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.provider.CookieProvider;
import roomescape.provider.TokenProvider;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;
    private CookieProvider cookieProvider;
    private TokenProvider tokenProvider;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Member member, HttpServletResponse response) {
        String accessToken = tokenProvider.createAccessToken(member);
        Cookie cookie = cookieProvider.createCookie(accessToken);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = cookieProvider.extractTokenFromCookie(cookies);

        LoginResponse loginResponse = new LoginResponse(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().get("name").toString());

        return ResponseEntity.ok()
                .body(loginResponse);
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
