package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.service.AuthService;
import roomescape.auth.web.Login;
import roomescape.auth.web.TokenExtractor;
import roomescape.util.CookieUtil;

@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;
    private final TokenExtractor tokenExtractor;

    public AuthController(AuthService authService, CookieUtil cookieUtil, TokenExtractor tokenExtractor) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
        this.tokenExtractor = tokenExtractor;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest,
                                      HttpServletResponse response) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        setTokenCookies(response, tokenResponse);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/refresh")
    public ResponseEntity<Void> loginByRefreshToken(HttpServletRequest request,
                                                    HttpServletResponse response) {
        String refreshToken = tokenExtractor.extractRefreshToken(request.getCookies());

        TokenResponse tokenResponse = authService.reissue(refreshToken);
        setTokenCookies(response, tokenResponse);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@Login LoginMember loginMember) {
        return ResponseEntity.ok(new LoginCheckResponse(loginMember.name()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Login LoginMember loginMember, HttpServletResponse response) {
        authService.logout(loginMember.id());
        cookieUtil.expireAccessTokenCookie(response);
        cookieUtil.expireRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    private void setTokenCookies(HttpServletResponse response, TokenResponse tokenResponse) {
        cookieUtil.setAccessTokenCookie(response, tokenResponse.accessToken());
        cookieUtil.setRefreshTokenCookie(response, tokenResponse.refreshToken());
    }
}
