package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.CookieManager;
import roomescape.auth.AuthService;
import roomescape.member.dto.AuthUserNameResponse;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.LoginResponse;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final CookieManager cookieManager;

    public MemberController(MemberService memberService, AuthService authService, CookieManager cookieHandler) {
        this.memberService = memberService;
        this.authService = authService;
        this.cookieManager = cookieHandler;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        cookieManager.setCookie(loginResponse.token(), 100, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthUserNameResponse> getAuthenticatedInfo(HttpServletRequest request) {
        Cookie cookie = cookieManager.getCookie(request);

        AuthUserNameResponse checkResponse = authService.findByToken(cookie.getValue());
        return ResponseEntity.ok().body(checkResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // TODO 뭘 null, 0 ???
        cookieManager.setCookie(null, 0, response);
        return ResponseEntity.ok().build();
    }

}
