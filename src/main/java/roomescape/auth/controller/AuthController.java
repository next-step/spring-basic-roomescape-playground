package roomescape.auth.controller;

import static roomescape.auth.util.AuthUtil.extractToken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberDetailResponse> checkLogin(HttpServletRequest request) {
        String token = extractToken(request);

        if (token.isEmpty()) {
            throw new IllegalArgumentException("로그인을 해주세요.");
        }

        MemberDetailResponse memberResponse = authService.checkLogin(token);
        return ResponseEntity.ok(memberResponse);
    }

    private void addCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
