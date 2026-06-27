package roomescape.member;

import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.loginmember.LoginMember;
import roomescape.token.CookieTokenUtils;
import roomescape.token.TokenUtils;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final TokenUtils tokenUtils;

    public MemberController(MemberService memberService, CookieTokenUtils cookieTokenUtils) {
        this.memberService = memberService;
        this.tokenUtils = cookieTokenUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberRequest memberRequest,
                                      HttpServletResponse httpServletResponse) {
        String token = memberService.createToken(memberRequest.email(), memberRequest.password());
        tokenUtils.appendToken(token, httpServletResponse);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@LoginMember Member member) {
        MemberResponse memberResponse = new MemberResponse(member.getId(), member.getName(), member.getEmail());
        return ResponseEntity.ok().body(memberResponse);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        tokenUtils.removeToken(httpServletResponse);
        return ResponseEntity.ok().build();
    }
}
