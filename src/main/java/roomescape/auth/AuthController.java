package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.MemberResponse;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        // cookie 생성
        Cookie cookie = new Cookie("token", tokenResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        MemberResponse memberResponse = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(memberResponse);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                return cookie.getValue();
            }
        }
        return "";
    }
}
