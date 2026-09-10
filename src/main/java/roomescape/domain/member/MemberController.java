package roomescape.domain.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.excpetion.UnauthorizedException;

import java.net.URI;
import java.util.Arrays;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        Member newMember = memberService.createMember(memberRequest.name(), memberRequest.email(), memberRequest.password());
        return ResponseEntity.created(URI.create("/members/" + newMember.getId())).body(MemberResponse.from(newMember));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletResponse httpServletResponse
    ) {

        String accessToken = authService.login(request.email(), request.password());

        httpServletResponse.addCookie(setCookie(accessToken));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public UsernameResponse getName(
            HttpServletRequest request
    ) {
        Cookie tokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(UnauthorizedException::new);

        return new UsernameResponse(authService.getUsername(tokenCookie.getValue()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = setCookie("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie setCookie(String value) {
        Cookie cookie = new Cookie("token", value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
