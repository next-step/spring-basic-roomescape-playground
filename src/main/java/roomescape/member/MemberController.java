package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MemberController {

    private static final String COOKIE_NAME = "token";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_VALID_TIME = 3600;

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(final @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = memberService.login(request);

        final Cookie cookie = new Cookie(COOKIE_NAME, loginResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_VALID_TIME);
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
