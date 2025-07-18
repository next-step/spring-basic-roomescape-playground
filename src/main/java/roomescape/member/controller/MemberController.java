package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.LoginResponse;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.LoginService;
import roomescape.member.service.MemberService;
import roomescape.util.CookieUtil;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;
    private LoginService loginService;

    public MemberController(MemberService memberService, LoginService loginService) {
        this.memberService = memberService;
        this.loginService = loginService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        // 1. email과 password로 멤버 조회 -> 조회한 멤버로 토큰 만듦
        String token = loginService.login(request.email(), request.password());

        // 2. cookie를 만들어 응답
        Cookie tokenCookie = CookieUtil.createTokenCookie(token);
        response.addCookie(tokenCookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> checkLogin(@CookieValue(name = "token", required = false) String token) {
        // 1. Cookie에서 토큰 정보 추출 -> 멤버를 찾아 멤버 정보를 응답
        LoginResponse response = loginService.getMemberFromToken(token);
        return ResponseEntity.ok(response);
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
