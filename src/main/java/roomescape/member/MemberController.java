package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class MemberController {

    private MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
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

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = memberService.login(request);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@CookieValue(name = "token", required = false) String token) {
        if (token == null) {
            // 토큰이 없는 경우 예외 처리나 다른 로직을 수행할 수 있습니다.
            // 여기서는 간단히 이름 없는 응답을 보내거나, 예외를 던질 수 있습니다.
            // 예를 들어, 권한 없음 에러(401)를 반환할 수 있습니다.
            return ResponseEntity.status(401).build();
        }

        Long memberId = Long.valueOf(jwtTokenProvider.getSubject(token));
        Member member = memberService.findById(memberId);

        return ResponseEntity.ok(new LoginCheckResponse(member.getName()));
    }
}
