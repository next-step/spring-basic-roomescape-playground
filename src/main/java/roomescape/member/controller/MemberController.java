package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.auth.AuthMember;
import roomescape.member.entity.Member;
import roomescape.member.service.MemberService;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody MemberRequest memberRequest,
            HttpServletResponse response) {

        String tokenValue = memberService.login(memberRequest);

        Cookie cookie = new Cookie("token", tokenValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .header("Keep-Alive", "timeout=60")
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> check(
            @AuthMember Member member
    ) {
        return ResponseEntity.ok()
                .header("Connection", "keep-alive")
                .header("Keep-Alive", "timeout=60")
                .body(new MemberResponse(null,member.getName(),null));
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
}
