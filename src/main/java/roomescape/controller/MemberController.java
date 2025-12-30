package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import roomescape.auth.AuthCookieProvider;
import roomescape.auth.LoginMember;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.model.Member;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
public class MemberController {
    private final AuthService authService;
    private final MemberService memberService;
    private final AuthCookieProvider authCookieProvider;

    public MemberController(MemberService memberService, AuthService authService, AuthCookieProvider authCookieProvider) {
        this.memberService = memberService;
        this.authService = authService;
        this.authCookieProvider = authCookieProvider;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.create(request);

        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.createToken(request);

        response.addCookie(authCookieProvider.create(token));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@LoginMember Member member) {
        return ResponseEntity.ok(new MemberResponse(member.getId(), member.getName(), member.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(authCookieProvider.expire());

        return ResponseEntity.ok().build();
    }
}
