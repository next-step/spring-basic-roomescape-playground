package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private static final String ACCESS_TOKEN_NAME = "token";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = authService.loginWithEmailAndPassword(loginRequest.email(), loginRequest.password());
        addCookie(response, ACCESS_TOKEN_NAME, token);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity loginCheck(@CookieValue(ACCESS_TOKEN_NAME) String accessToken) {
        return ResponseEntity.ok().body(authService.loginCheckWithToken(accessToken));
    }

    private void addCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
