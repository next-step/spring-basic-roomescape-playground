package roomescape.member;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.CookieManager;

import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final CookieManager cookieManager;

    public MemberController(MemberService memberService, CookieManager cookieManager) {
        this.memberService = memberService;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.id())).body(member);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieManager.deleteToken(response);
        return ResponseEntity.noContent().build();
    }
}
