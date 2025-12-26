package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.exception.UnauthorizedException;
import roomescape.jwt.JwtTokenProvider;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
public class MemberController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;

    public MemberController(MemberService memberService, AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);

        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.createToken(request);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60); // 1h

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookies(request.getCookies());

        if (token == null) throw new UnauthorizedException("로그인이 필요합니다.");

        String subject = jwtTokenProvider.getSubject(token);
        MemberResponse memberResponse = memberService.findById(subject);

        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter((cookie) -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
