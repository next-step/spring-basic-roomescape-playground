package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.TokenRequest;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        String token = memberService.login(tokenRequest);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Keep-Alive", "timeout=60")
                .header("Set-Cookie", "token=" + token + "; Path=/; HttpOnly")
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> getLoginMemberInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Member member = memberService.getLoginMemberInfo(cookies);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONNECTION, "keep-alive")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.DATE, String.valueOf(new Date()))
                .header("Keep-Alive", "timeout=60")
                .header(HttpHeaders.TRANSFER_ENCODING, "chunked")
                .body(new MemberResponse(member.getId(),member.getName(),member.getEmail()));
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
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

}
