package roomescape.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.CookieManager;
import roomescape.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberResponse;

@RestController
public class LoginController {
    private final LoginService loginService;
    private final JwtProvider jwtProvider;
    private final CookieManager cookieManager;

    public LoginController(LoginService loginService, JwtProvider jwtProvider, CookieManager cookieManager) {
        this.jwtProvider = jwtProvider;
        this.loginService = loginService;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpresponse) {
        Member member = loginService.login(loginRequest.email(), loginRequest.password());

        String accessToken = jwtProvider.createAccessToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        httpresponse.addCookie(accessCookie);
        httpresponse.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String refreshToken = cookieManager.extractToken(cookies, "refreshToken");

        Long memberId = jwtProvider.getMemberId(refreshToken);

        Member member = loginService.findById(memberId);

        String accessToken = jwtProvider.createAccessToken(member);

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(LoginMember loginMember) {
        Long memberId = loginMember.id();
        MemberResponse memberResponse = loginService.checkLogin(memberId);
        return ResponseEntity.ok(memberResponse);
    }
}
