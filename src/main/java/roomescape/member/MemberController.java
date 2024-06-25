package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.*;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;



    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody
            LoginRequest loginRequest,
            HttpServletResponse httpServletResponse
    ){
        String token = memberService.login(loginRequest);
        Cookie cookie = new Cookie("token",token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.status(200).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> checkLogin(
            @CookieValue("token") String token
    ){
        LoginResponse loginResponse = memberService.checkLogin(token);

        return ResponseEntity.status(200)
                .body(loginResponse);
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
