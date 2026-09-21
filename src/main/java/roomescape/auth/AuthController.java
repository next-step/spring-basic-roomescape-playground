package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberService;

@RestController
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public AuthController(AuthService authService, MemberService memberService, TokenProvider tokenProvider) {
        this.authService = authService;
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Member member = authService.authenticateMember(loginRequest);
        String token = tokenProvider.createToken(member);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(LoginMember loginMember) {
        Member member = memberService.getMemberById(loginMember.getId());

        LoginCheckResponse loginCheckResponse = LoginCheckResponse.from(member);

        return ResponseEntity.ok(loginCheckResponse);
    }
}
