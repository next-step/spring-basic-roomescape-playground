package roomescape.member;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthCookieProvider;
import roomescape.auth.LoginMember;
import roomescape.auth.LoginMemberInfo;

import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final AuthCookieProvider authCookieProvider;

    public MemberController(MemberService memberService, AuthCookieProvider authCookieProvider) {
        this.memberService = memberService;
        this.authCookieProvider = authCookieProvider;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login") // URL 경로
    // HTTP 요청형식
    public ResponseEntity login(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        try {
            String token = memberService.login(memberRequest);
            response.addCookie(authCookieProvider.createLoginCookie(token));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/login/check")
    public ResponseEntity checkLogin(@LoginMember LoginMemberInfo member) {
        return ResponseEntity.ok(new MemberResponse(member.getId(), member.getName(), member.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        response.addCookie(authCookieProvider.createLogoutCookie());
        return ResponseEntity.ok().build();
    }
}
