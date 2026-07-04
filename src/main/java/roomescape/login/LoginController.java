package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberResponse;

@RestController
public class LoginController {
    private final LoginService loginService;
    private final JwtProvider jwtProvider;

    public LoginController(LoginService loginService, JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpresponse) {
        Member member = loginService.login(loginRequest.email(), loginRequest.password());

        String token = jwtProvider.createToken(member);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpresponse.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(LoginMember loginMember) {
        Long memberId = loginMember.id();
        MemberResponse memberResponse = loginService.checkLogin(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new RuntimeException("Invalid cookies");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new RuntimeException("Token not found");
    }

}
