package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.MemberService;
import roomescape.member.dto.MemberLoginCheckResponse;
import roomescape.member.dto.MemberLoginRequest;

@RestController
public class AuthController {

    private final MemberService memberService;
    private final JwtTokenManager jwtTokenManager;

    public AuthController(final MemberService memberService,
                          final JwtTokenManager jwtTokenManager) {
        this.memberService = memberService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequest request,
                                HttpServletResponse response) {
        final String token = memberService.findMember(request); // 요청받은 email과 비밀번호로 사용자 조회 후, 토큰 발급

        Cookie cookie = new Cookie(AuthConstant.TOKEN.getValue(), token); // 해당 토큰을 담은 쿠키 생성
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .header("Keep-Alive", "timeout=60")
                .header("Content-Type","application/json")
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity loginCheck(@CookieValue(value = "token") String token) {
        final String memberName = jwtTokenManager.getValueFromJwtToken(token, AuthConstant.NAME);
        return ResponseEntity.ok().body(new MemberLoginCheckResponse(memberName));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthConstant.TOKEN.getValue(), "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
