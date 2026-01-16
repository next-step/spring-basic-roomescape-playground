package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import auth.JwtUtils;
import roomescape.util.CookieUtil;

import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    public MemberController(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequestDto memberRequest) {
        MemberResponseDto member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberRequestDto memberRequest, HttpServletResponse response) {
        Member member = memberService.login(memberRequest.email(), memberRequest.password());

        String accessToken = createToken(member);

        Cookie cookie = CookieUtil.createHttpOnlyCookie("token", accessToken, JwtUtils.DEFAULT_MAX_AGE_SECONDS, false);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponseDto> checkLogin(HttpServletRequest request) {
        String token = jwtUtils.extractTokenFromCookies(request.getCookies());

        String name = jwtUtils.parseClaims(token).get("name", String.class);

        MemberResponseDto body = new MemberResponseDto(null, name, null);
        return ResponseEntity.ok(body);
    }

    public String createToken(Member member) {
        return jwtUtils.createToken(member.getId().toString(), member.getName(), member.getRole().name());
    }

    public String createTokenFromEmailAndPassword(String email, String password) {
        Member member = memberService.login(email, password);
        return jwtUtils.createToken(member.getId().toString(), member.getName(), member.getRole().name());
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = CookieUtil.expireCookie("token");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
