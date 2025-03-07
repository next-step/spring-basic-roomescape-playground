package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        MemberResponse memberResponse = memberService.findByEmailAndPassword(request);
        ResponseCookie cookie = ResponseCookie.from("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI")
                .path("/")
                .httpOnly(true)
                .build();
        System.out.println(">>>>cookie = " + cookie);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
        //응답에 Cookie에
        // "token"값으로 토큰이 포함되도록 하세요.
        //HTTP/1.1 200 OK
        //Content-Type: application/json
        //Keep-Alive: timeout=60
        //Set-Cookie: token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIi...; Path=/; HttpOnly
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
