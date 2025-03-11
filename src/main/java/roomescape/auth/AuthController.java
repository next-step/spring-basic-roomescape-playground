package roomescape.auth;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@RestController
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    public AuthController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping("/login/check")
    public ResponseEntity<Member> checkLoginStatus(HttpServletRequest request) {
        String token = authService.getTokenFromCookies(request.getCookies());

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Long userId = authService.getUserIdFromToken(token);
        Member foundMember = memberService.findById(userId);

        return ResponseEntity.ok(foundMember);
    }
}
