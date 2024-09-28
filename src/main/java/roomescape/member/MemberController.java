package roomescape.member;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.auth.CookieUtils;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final CookieUtils cookieUtils;

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
        cookieUtils.setToken(response, memberService.memberLogin(request));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberLoginCheckResponse> checkLogin(LoginMember loginMember) {
        MemberLoginCheckResponse response = new MemberLoginCheckResponse(loginMember.name());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        cookieUtils.setTokenNull(response);
        return ResponseEntity.ok().build();
    }
}
