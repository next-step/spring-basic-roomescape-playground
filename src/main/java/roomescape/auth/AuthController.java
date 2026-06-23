package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.DTO.LoggedInUserResponse;
import roomescape.auth.DTO.LoginRequest;
import roomescape.auth.config.AuthenticationPrincipal;
import roomescape.auth.config.CookieProvider;
import roomescape.member.DTO.MemberResponse;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoggedInUserResponse> getLoggedInUsername(@AuthenticationPrincipal MemberResponse member) {
        return ResponseEntity.ok(new LoggedInUserResponse(member.getName()));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        response.addCookie(CookieProvider.provideCookie(token));
        return ResponseEntity.ok().build();
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
