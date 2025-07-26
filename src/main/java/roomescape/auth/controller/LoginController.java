package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.request.LoginRequest;
import roomescape.auth.response.LoginResponse;
import roomescape.auth.service.LoginService;
import roomescape.member.Member;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(
        @RequestBody LoginRequest loginRequest, HttpServletResponse response
    ) {
        String token = loginService.login(loginRequest.email(), loginRequest.password());

        ResponseCookie cookie = ResponseCookie.from("token", token)
            .path("/")
            .httpOnly(true)
            .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> loginCheck(
        @CookieValue(value = "token", required = false) String token
    ) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        Member member = loginService.check(token);
        return ResponseEntity.ok(new LoginResponse(member.getName()));
    }
}
