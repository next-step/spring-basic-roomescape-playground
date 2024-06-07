package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import roomescape.auth.AuthorizationProvider;
import roomescape.auth.MemberAuthorization;

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
        MemberAuthorization memberAuthorization = authorizationProvider.createByPayload(memberLoginRequest.email());
        Cookie cookie = new Cookie("token", memberAuthorization.authorization());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
