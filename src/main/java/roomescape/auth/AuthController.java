package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<?> login(@RequestBody MemberRequest request, HttpServletResponse response) {
        return authService.login(request, response);
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        return authService.checkLogin(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        return authService.logout(response);
    }
}

