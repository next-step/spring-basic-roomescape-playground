package roomescape.auth.controller;

import static roomescape.auth.util.AuthUtil.extractToken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.service.AuthService;
import roomescape.auth.dto.AuthRequest;
import roomescape.auth.dto.MemberDetailResponse;

@RestController
public class AuthController {
    public static final String TOKEN_NAME = "token";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        String token = authService.login(authRequest).token();
        addCookie(response, token);

        String redirectUrl = findRedirectUrl(token);

        return ResponseEntity.status(200).header("Location", redirectUrl).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberDetailResponse> checkLogin(HttpServletRequest request) {
        String token = extractToken(request);
        MemberDetailResponse memberResponse = authService.checkLogin(token);

        return ResponseEntity.ok(memberResponse);
    }

    private void addCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String findRedirectUrl(String token) {
        MemberDetailResponse memberDetailResponse = authService.checkLogin(token);
        if (memberDetailResponse.role().isAdmin()) {
            return "/admin";
        }
        return "/";
    }
}
