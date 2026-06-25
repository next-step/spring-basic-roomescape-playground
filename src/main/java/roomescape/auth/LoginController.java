package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@RestController
public class LoginController {

    private final MemberDao memberDao;
    private final TokenService tokenService;

    public LoginController(MemberDao memberDao, TokenService tokenService) {
        this.memberDao = memberDao;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request,
                                      HttpServletResponse response) {
        Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());
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
