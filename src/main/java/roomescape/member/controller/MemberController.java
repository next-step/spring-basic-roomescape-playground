package roomescape.member.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.auth.Auth;
import roomescape.auth.CookieUtils;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.MemberLoginCheckResponse;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CookieUtils cookieUtils;

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
        @RequestBody MemberLoginRequest request,
        HttpServletResponse response
    ) {
        cookieUtils.setToken(response, memberService.memberLogin(request));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberLoginCheckResponse> checkLogin(@Auth LoginMember loginMember) {
        MemberLoginCheckResponse response = new MemberLoginCheckResponse(loginMember.name());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        cookieUtils.setTokenNull(response);
        return ResponseEntity.ok().build();
    }
}
