package roomescape.UI;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.AuthService;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.infrastructure.BearerAuthorizationExtractor;
import roomescape.member.MemberResponse;

@RestController
public class TokenLoginController {
    private final AuthService authService;
    private final AuthorizationExtractor<String> authorizationExtractor;

    public TokenLoginController(AuthService authService) {
        this.authService = authService;
        this.authorizationExtractor = new BearerAuthorizationExtractor();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> tokenLogin(@RequestBody TokenRequest tokenRequest, HttpServletResponse response){
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = new Cookie("token", tokenResponse.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    boolean isValid = authService.verifyToken(token);
                    if (isValid) {
                        String email = authService.getEmailFromToken(token);
                        MemberResponse memberResponse = authService.findMemberByEmail(email);
                        return ResponseEntity.ok(memberResponse);
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
