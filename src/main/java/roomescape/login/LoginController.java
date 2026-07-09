package roomescape.login;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberResponse;
import roomescape.token.CookieBuilder;
import roomescape.token.TokenProvider;

@RestController
public class LoginController {
    private final MemberRepository memberRepository;

    public LoginController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest){
        Member member = memberRepository.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ).orElseThrow(() -> new IllegalArgumentException());

        String token = TokenProvider.createToken(member);
        ResponseCookie cookie = CookieBuilder.createTokenCookie(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(LoginMember loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new MemberResponse(loginMember.getName()));
    }
}
