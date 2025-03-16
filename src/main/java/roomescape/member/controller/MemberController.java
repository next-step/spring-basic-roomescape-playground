package roomescape.member.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import roomescape.auth.CookieManager;
import roomescape.member.LoginResponse;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final String AUTH_TOKEN_COOKIE = "token";

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {

        LoginResponse loginResponse = memberService.login(memberRequest.getEmail(), memberRequest.getPassword());
        CookieManager.createCookie(response, AUTH_TOKEN_COOKIE, loginResponse.getAccessToken());

        return ResponseEntity.ok("Login successful");

    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AUTH_TOKEN_COOKIE, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
