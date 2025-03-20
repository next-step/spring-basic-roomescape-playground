package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.service.AuthService;
import roomescape.common.cookie.CookieManager;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.dto.response.LoginCheckResponse;
import roomescape.member.dto.response.LoginResponse;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse login = authService.login(request);

        Cookie cookie = CookieManager.createCookie(CookieManager.AUTH_TOKEN_COOKIE, login.accessToken());
        response.addCookie(cookie);

        return ResponseEntity.ok(login);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(LoginMember loginMember) {
        LoginCheckResponse loginCheck = authService.loginCheck(loginMember);
        return ResponseEntity.ok(loginCheck);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = CookieManager.createCookie(CookieManager.AUTH_TOKEN_COOKIE, "");
        response.addCookie(cookie);

        return ResponseEntity.noContent()
                .build();
    }
}
