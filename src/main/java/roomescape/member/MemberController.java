package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    public static final String COOKIE_NAME = "token";
    public static final String EMPTY_VALUE = "";
    public static final String ROOT_URI = "/";

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = memberService.login(request);

        ResponseCookie cookie = createCookie(loginResponse);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    private ResponseCookie createCookie(LoginResponse accessToken) {
        return ResponseCookie.from(COOKIE_NAME, accessToken.token())
                .path(ROOT_URI)
                .httpOnly(true)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthUserNameResponse> getAuthenticatedInfo(@CookieValue(name = COOKIE_NAME) String token) {
        AuthUserNameResponse checkResponse = memberService.findByToken(token);
        return ResponseEntity.ok().body(checkResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, EMPTY_VALUE);
        cookie.setHttpOnly(true);
        cookie.setPath(ROOT_URI);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
