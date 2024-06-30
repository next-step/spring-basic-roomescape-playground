package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.member.LoginMember;
import roomescape.member.MemberService;

@Controller
public class AuthController {
    MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> tokenLogin(@RequestBody LoginRequest request, HttpServletResponse response) {

        String accessToken = memberService.loginByEmailAndPassword(request);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginMember> loginCheck(HttpServletRequest request) {

        LoginMember loginMember = memberService.checkLogin(request);

        return ResponseEntity.ok().body(loginMember);
    }
}
