package roomescape.domain.auth.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.service.AuthService;
import roomescape.domain.auth.web.support.Login;
import roomescape.domain.auth.web.support.SessionManager;
import roomescape.domain.auth.web.dto.AuthRequest;
import roomescape.domain.auth.web.dto.UsernameResponse;

@RestController
public class AuthController {

    private final AuthService authService;
    private final SessionManager sessionManager;

    public AuthController(AuthService authService, SessionManager sessionManager) {
        this.authService = authService;
        this.sessionManager = sessionManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpServletRequest
    ) {

        LoginMember loginMember = authService.login(request.email(),  request.password());

        sessionManager.store(httpServletRequest, loginMember);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public UsernameResponse getName(
            @Login LoginMember member
    ) {
        return new UsernameResponse(member.getName());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        sessionManager.clear(request);

        Cookie expiredCookie = new Cookie("JSESSIONID", "");
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);

        return ResponseEntity.ok().build();
    }
}
