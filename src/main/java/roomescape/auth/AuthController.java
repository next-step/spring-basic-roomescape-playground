package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.Member;

@RestController
public class AuthController {
    private final AuthService authService;
    private final TokenGenerator tokenGenerator;

    public AuthController(
            @Autowired  AuthService authService,
            @Autowired  TokenGenerator tokenGenerator
    ) {
        this.authService = authService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody LoginRequestDto memberLoginRequest,
            HttpServletResponse response
    ) {
        Member member = authService.loginByEmailAndPassword(memberLoginRequest);

        String tokenValue = tokenGenerator.generateAccessToken(member);

        Cookie cookie = new Cookie("token", tokenValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 5);
        cookie.setSecure(true);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity loginCheck(
            @CookieValue("token") String token
    ) {
        AuthCredential tokenInfo = tokenGenerator.parseAccessToken(token);

       authService.loginCheck(tokenInfo.id(), tokenInfo);

        return ResponseEntity
                .ok()
                .body(new LoginCheckResponseDto(tokenInfo.name()));
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
