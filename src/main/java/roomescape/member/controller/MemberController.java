package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.login.LoginMemberName;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.service.MemberService;

import java.util.Date;

@RestController
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.LOCATION, "/members/" + member.id())
                .body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        String token = memberService.login(memberRequest);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.SET_COOKIE, "token=" + token + "; HttpOnly; Path=/")
                .header("Keep-Alive", "timeout=60")
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@LoginMemberName String memberName) {
        MemberResponse memberResponse = memberService.getMemberByName(memberName);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Connection", "keep-alive")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Keep-Alive", "timeout=60")
                .header("Transfer-Encoding", "chunked")
                .header("Date", new Date().toString())
                .body(memberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}
