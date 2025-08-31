package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.JwtProvider;
import roomescape.login.LoginRequest;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@RestController
public class LoginController {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public LoginController(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Member member = memberDao.findByEmailAndPassword(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            String token = jwtProvider.generateToken(
                    member.getId(),
                    member.getEmail(),
                    member.getRole()
            );

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
