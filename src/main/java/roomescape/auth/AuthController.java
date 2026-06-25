package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.AuthResult;
import roomescape.auth.dto.MemberInfo;
import roomescape.member.MemberRequest;

import java.net.URI;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberInfo> createMember(@RequestBody MemberRequest memberRequest) {
        AuthResult result = authService.signUp(memberRequest);
        ResponseCookie tokenCookie = createTokenCookie(result.token(), 3600);

        return ResponseEntity
                .created(URI.create("/members/" + result.memberInfo().id()))
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .body(result.memberInfo());
    }

    @PostMapping("/login")
    public ResponseEntity<MemberInfo> login(@RequestBody MemberRequest memberRequest) {
        AuthResult result = authService.login(memberRequest);
        ResponseCookie tokenCookie = createTokenCookie(result.token(), 3600);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .body(result.memberInfo());
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberInfo> checklogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractToken(cookies);
        AuthResult result = authService.loginCheck(token);

        return ResponseEntity
                .ok()
                .body(result.memberInfo());
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        ResponseCookie tokenCookie = createTokenCookie(null, 0);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .build();
    }

    private ResponseCookie createTokenCookie(String token, int maxAge) {
        return ResponseCookie
                .from("token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    private String extractToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}