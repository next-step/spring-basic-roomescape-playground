package roomescape.auth.controller;

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
import roomescape.member.MemberInfo;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(
        @RequestBody LoginRequest loginRequest
    ) {
        String token = loginService.login(loginRequest.email(), loginRequest.password());

        ResponseCookie cookie = ResponseCookie.from("token", token)
            .path("/")
            .httpOnly(true)
            .build();

        return ResponseEntity.ok()
            .header("Set-Cookie", cookie.toString())
            .build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> loginCheck(
        @CookieValue(value = "token", required = false) String token
    ) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        MemberInfo memberInfo = loginService.check(token);
        return ResponseEntity.ok(new LoginResponse(memberInfo.name()));
    }
}
