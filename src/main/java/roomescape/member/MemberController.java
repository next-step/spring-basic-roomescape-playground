package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.global.auth.AuthResponse;
import roomescape.global.auth.CookieUtil;
import roomescape.global.auth.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final CookieUtil cookieUtil;

    @Autowired
    public MemberController(MemberService memberService, CookieUtil cookieUtil) {
        this.memberService = memberService;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity login(
        @RequestBody LoginRequest request,
        HttpServletResponse response
    ) {
        String token = memberService.memberLogin(request);
        cookieUtil.addCookie(response, "token", token, 3600);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthResponse> checkLogin(HttpServletRequest request) {
        String token = cookieUtil.extractTokenFromCookie(request.getCookies());
        Member member = memberService.getAuth(token);
        var response = AuthResponse.from(member);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        cookieUtil.addCookie(response, "token", "", 0);
        return ResponseEntity.ok().build();
    }
}
