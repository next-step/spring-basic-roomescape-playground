package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> login(@RequestBody MemberRequest request, HttpServletResponse response) {
        try {
            Member member = authService.login(request, response);
            return ResponseEntity.ok(new MemberResponse(member.getId(), member.getName(), member.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Member member = authService.checkLogin(request);
        if (member == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new MemberResponse(null, member.getName(), null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }
}

