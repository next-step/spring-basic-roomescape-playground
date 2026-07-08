package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.exception.AuthenticationException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberResponse;

@RestController
public class LoginController {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public LoginController(MemberRepository memberRepository, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request,
                                      HttpServletResponse response) {
        Member member = memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new AuthenticationException("잘못된 이메일 또는 비밀번호입니다."));
        String token = tokenService.createToken(member);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(LoginMember loginMember) {
        return ResponseEntity.ok(new MemberResponse(loginMember.getId(), loginMember.getName(), loginMember.getEmail()));
    }
}
