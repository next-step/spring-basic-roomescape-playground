package roomescape.member;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.util.CookieUtil;
import roomescape.util.JwtTokenProvider;

import java.util.Map;

@RestController
public class LoginController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Member member = memberService.login(request.email(), request.password());

        String token = jwtTokenProvider.generateToken(member);
        response.addCookie(CookieUtil.createToken(token));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<Map<String, String>> checkLogin(LoginMember loginMember) {
        return ResponseEntity.ok(Map.of(
                "name", loginMember.name(),
                "role", loginMember.role()
        ));
    }
}
