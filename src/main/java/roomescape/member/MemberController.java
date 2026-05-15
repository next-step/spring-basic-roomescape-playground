package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import roomescape.auth.AuthService;
import roomescape.auth.LoginCheckResponse;
import roomescape.auth.LoginRequest;
import roomescape.auth.LoginResponse;

@RestController
public class MemberController {

    private MemberService memberService;
    private AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/login/check")
    public ResponseEntity checkLogin(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String token = extractTokenFromCookie(cookies);
        Long memberId = authService.getMemberId(token);
        LoginCheckResponse response = memberService.getLoginCheckInfo(memberId);

        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    // TODO: 사용자 권한 에러 추가
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest,
            HttpServletResponse httpServletResponse) {
        Member member = memberService.getMemberWithLoginRequest(loginRequest);
        LoginResponse loginResponse = authService.createToken(member);

        Cookie cookie = new Cookie("token", loginResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok().build();
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

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
