package roomescape.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@RestController
public class LoginController {
    private final MemberDao memberDao;

    public LoginController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok().build();
    }

}
