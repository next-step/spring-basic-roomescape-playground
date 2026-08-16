package roomescape.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthCookieProvider;
import roomescape.auth.AuthUser;
import roomescape.auth.LoginMemberInfo;
import roomescape.auth.LoginTokens;

import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final AuthCookieProvider authCookieProvider;

    public MemberController(MemberService memberService, AuthCookieProvider authCookieProvider) {
        this.memberService = memberService;
        this.authCookieProvider = authCookieProvider;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/login") // URL 경로
    public ResponseEntity<Void> login(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        LoginTokens tokens = memberService.login(memberRequest);
        response.addCookie(authCookieProvider.createAccessTokenCookie(tokens.accessToken()));
        response.addCookie(authCookieProvider.createRefreshTokenCookie(tokens.refreshToken()));
        response.addCookie(authCookieProvider.createLoginCookie(tokens.accessToken()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authCookieProvider.extractRefreshToken(request);
        String accessToken = memberService.refreshAccessToken(refreshToken);
        response.addCookie(authCookieProvider.createAccessTokenCookie(accessToken));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@AuthUser LoginMemberInfo member) {
        return ResponseEntity.ok(new MemberResponse(member.id(), member.name(), member.email()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(authCookieProvider.createLogoutAccessTokenCookie());
        response.addCookie(authCookieProvider.createLogoutRefreshTokenCookie());
        response.addCookie(authCookieProvider.createLogoutCookie());

        return ResponseEntity.ok().build();
    }
}
