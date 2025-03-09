package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.request.MemberRequest;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.service.MemberService;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.dto.response.LoginCheckResponse;
import roomescape.member.dto.response.LoginResponse;

import java.net.URI;

@RestController
public class MemberController {

    private static final String COOKIE_NAME = "token";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_VALID_TIME = 3600;

    private final MemberService memberService;

    public MemberController( MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId()))
                .body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse login = memberService.login(request);

        Cookie cookie = new Cookie(COOKIE_NAME, login.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_VALID_TIME);
        response.addCookie(cookie);

        return ResponseEntity.ok(login);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@CookieValue(value = COOKIE_NAME) Cookie cookie) {
        LoginCheckResponse loginCheck = memberService.loginCheck(cookie);
        return ResponseEntity.ok(loginCheck);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
