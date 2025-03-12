package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthService;
import roomescape.member.dto.AuthUserNameResponse;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.LoginResponse;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@RestController
public class MemberController {

    public static final String COOKIE_NAME = "token";
    public static final String EMPTY_VALUE = "";
    public static final String ROOT_URI = "/";

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        createCookie(loginResponse.token(), response);
        return ResponseEntity.ok().build();
    }

    private void createCookie(String accessToken, HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, accessToken);
        cookie.setPath(ROOT_URI);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthUserNameResponse> getAuthenticatedInfo(@CookieValue(name = COOKIE_NAME) String token) {
        AuthUserNameResponse checkResponse = authService.findByToken(token);
        return ResponseEntity.ok().body(checkResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        deleteCookie(response);
        return ResponseEntity.ok().build();
    }

    private void deleteCookie(HttpServletResponse response) {
        createCookie(null, response);
    }

}
