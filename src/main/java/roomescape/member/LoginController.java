package roomescape.member;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.util.CookieUtil;
import roomescape.util.JwtUtil;

import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public LoginController(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password());

        String token = jwtUtil.generateToken(member);
        response.addCookie(CookieUtil.createTokenCookie(token));

        log.info("로그인 성공: memberId={}, email={}", member.getId(), member.getEmail());
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
