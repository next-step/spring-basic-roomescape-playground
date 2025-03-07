package roomescape.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.net.URI;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    private MemberService memberService;
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String accessToken = memberService.authenticateAndGetToken(request);

        ResponseCookie cookie = createCookie(accessToken); // 생성된 토큰을 쿠키에 담기
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @NotNull
    private ResponseCookie createCookie(String accessToken) {
        return ResponseCookie.from("token", accessToken)
                .path("/")
                .httpOnly(true)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckResponse> getAuthenticatedInfo(@CookieValue(name = "token") String token) {
        CheckResponse checkResponse = memberService.findByToken(token);
        return ResponseEntity.ok().body(checkResponse);
    }

}
