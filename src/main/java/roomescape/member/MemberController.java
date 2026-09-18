package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.CookieTokenExtractor;
import roomescape.auth.JwtTokenProvider;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieTokenExtractor cookieTokenExtractor;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider,
                            CookieTokenExtractor cookieTokenExtractor) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieTokenExtractor = cookieTokenExtractor;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Member member = memberService.authenticate(loginRequest);
        String token = jwtTokenProvider.createToken(member.getEmail());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> getCurrentMember(LoginMember loginMember) {
        MemberResponse memberResponse = new MemberResponse(loginMember.getId(), loginMember.getName(),
                loginMember.getEmail());

        return ResponseEntity.ok().body(memberResponse);
    }

}
