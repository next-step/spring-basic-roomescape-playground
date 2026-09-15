package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberService;

@RestController
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public AuthController(AuthService authService, MemberService memberService, TokenProvider tokenProvider) {
        this.authService = authService;
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Member member = authService.authenticateMember(loginRequest);
        String token = tokenProvider.createToken(member);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        Long memberId = tokenProvider.extractMemberId(token);
        Member member = memberService.getMemberById(memberId);

        LoginCheckResponse loginCheckResponse = LoginCheckResponse.from(member);

        return ResponseEntity.ok(loginCheckResponse);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new RuntimeException("인증 정보가 존재하지 않습니다.");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        throw new RuntimeException("토큰이 존재하지 않습니다.");
    }
}
