package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.client.cookie.CookieResolver;
import roomescape.auth.client.cookie.CookieProvider;
import roomescape.auth.client.jwt.JwtProvider;
import roomescape.auth.client.jwt.JwtResolver;
import roomescape.member.Member;

@Controller
public class AuthController {

    private static final String EXPIRED_TOKEN = "";
    private static final Long DEFAULT_TIME = 60L;

    private final AuthService authService;
    private final CookieProvider cookieProvider;
    private final CookieResolver cookieResolver;
    private final JwtProvider jwtProvider;
    private final JwtResolver jwtResolver;

    public AuthController(AuthService authService, CookieProvider cookieProvider,
                          CookieResolver cookieResolver,
                          JwtProvider jwtProvider, JwtResolver jwtResolver) {
        this.authService = authService;
        this.cookieProvider = cookieProvider;
        this.cookieResolver = cookieResolver;
        this.jwtProvider = jwtProvider;
        this.jwtResolver = jwtResolver;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
        Member findMember = getMember(loginRequest);
        String accessToken = jwtProvider.generateToken(findMember.getId(),
                findMember.getName(), findMember.getRole());
        ResponseCookie responseCookie = cookieProvider.generateCookie(accessToken,
                Duration.ofMinutes(DEFAULT_TIME));

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = cookieResolver.getToken(cookies);
        String name = jwtResolver.getName(accessToken);

        return ResponseEntity.ok(new LoginCheckResponse(name));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie responseCookie = cookieProvider.generateCookie(EXPIRED_TOKEN, Duration.ZERO);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    private Member getMember(final LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();
        Member findMember = authService.findMemberByEmailAndPassword(email, password);
        return findMember;
    }
}
