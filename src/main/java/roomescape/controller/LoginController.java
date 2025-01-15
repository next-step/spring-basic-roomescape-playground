package roomescape.controller;

import auth.JwtAuthManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.login.LoginCheckResponse;
import roomescape.domain.login.LoginRequest;
import roomescape.domain.login.LoginResponse;
import roomescape.domain.member.Member;

@RestController
public class LoginController {
    private final JwtAuthManager jwtAuthManager;

    public LoginController(JwtAuthManager jwtAuthManager) {
        this.jwtAuthManager = jwtAuthManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest LoginRequest, HttpServletResponse response) {
        String token = jwtAuthManager.createToken(LoginRequest.getEmail());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        LoginResponse loginResponse = new LoginResponse(token);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(Member loginMember) {

        LoginCheckResponse loginCheckResponse = new LoginCheckResponse(loginMember.getName());
        return ResponseEntity.ok().body(loginCheckResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}