package roomescape.auth.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.constants.AuthConstants;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.service.AuthService;
import roomescape.auth.util.CookieManager;
import roomescape.exception.UnauthorizedAccessException;
import roomescape.member.dto.Member;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginMember> checkLoginStatus(HttpServletRequest request) {
        try {
            CookieManager cookieManager = new CookieManager(request.getCookies());
            String accessToken = cookieManager.getValue(AuthConstants.AUTH_TOKEN_COOKIE);
            Member member = authService.getLoginMember(accessToken);

            LoginMember loginMember = new LoginMember(
                    member.getId(),
                    member.getName(),
                    member.getEmail(),
                    member.getRole()
            );
            return ResponseEntity.ok(loginMember);
        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
