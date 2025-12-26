package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
public class MemberController {
    private final AuthService authService;
    private MemberService memberService;

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
        String token = authService.createToken(request);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60); // 1h

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
