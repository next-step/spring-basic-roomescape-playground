package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.global.auth.CookieUtils;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.member.controller.dto.MemberLoginRequest;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
        @RequestBody MemberLoginRequest request,
        HttpServletResponse response
    ) {
        String token = memberService.login(request);
        CookieUtils.setTokenCookie(token, response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<Member> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = CookieUtils.extractTokenFromCookie(cookies != null ? cookies : new Cookie[0]);

        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member response = memberService.checkLogin(token);

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
