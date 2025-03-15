package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthController(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLoginStatus(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String userEmail = jwtTokenProvider.getEmailFromToken(token);
        Member member = memberService.findByEmail(userEmail);

        MemberResponse memberResponse = new MemberResponse(member.getId(), member.getName(), member.getEmail());
        return ResponseEntity.ok(memberResponse);
    }

}
