package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import roomescape.auth.AuthorizationProvider;
import roomescape.auth.MemberAuthContext;
import roomescape.auth.MemberCredential;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final AuthorizationProvider authorizationProvider;

    public MemberController(MemberService memberService, AuthorizationProvider authorizationProvider) {
        this.memberService = memberService;
        this.authorizationProvider = authorizationProvider;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
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

    @PostMapping("/login")
    public void login(
            @RequestBody MemberLoginRequest memberLoginRequest,
            HttpServletResponse response
    ) {
        MemberAuthContext memberAuthContext = memberService.loginByEmailAndPassword(memberLoginRequest);
        MemberCredential memberCredential = authorizationProvider.create(memberAuthContext);
        Cookie cookie = new Cookie("token", memberCredential.authorization());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public MemberResponse loginCheck(
            @CookieValue("token") String token
    ) {
        MemberCredential memberCredential = new MemberCredential(token);
        MemberAuthContext authContext = authorizationProvider.parseCredential(memberCredential);
        return memberService.checkLogin(authContext);
    }
}
