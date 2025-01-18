package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@RestController
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthController(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthInfo authInfo, HttpServletResponse response) {
        try {
            Member member = memberDao.findByEmailAndPassword(authInfo.email(), authInfo.password());
            String token = jwtTokenProvider.createToken(member.getEmail());

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok().build();

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @GetMapping("/login/check")
    public ResponseEntity check(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String subject = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                subject = jwtTokenProvider.getSubject(token);
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("subject", subject);
        return ResponseEntity.ok().body(response);
    }
}
