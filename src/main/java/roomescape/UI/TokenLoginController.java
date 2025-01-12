package roomescape.UI;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.AuthService;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.member.Member;
import roomescape.member.MemberResponse;

@RestController
public class TokenLoginController {
    private final AuthService authService;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> tokenLogin(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie("token", tokenResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(Member loginMember) {
        MemberResponse memberResponse = new MemberResponse(
                loginMember.getId(),
                loginMember.getName(),
                loginMember.getEmail()
        );
        return ResponseEntity.ok(memberResponse);
    }
}
