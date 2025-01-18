package roomescape.auth;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            String token = jwtTokenProvider.createToken(member);

            Cookie tokenCookie = CookieUtils.createTokenCookie(token);
            response.addCookie(tokenCookie);
            return ResponseEntity.ok().build();

        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @GetMapping("/login/check")
    public ResponseEntity check(HttpServletRequest request) {
        String token = CookieUtils.findCookie(request.getCookies(), "token");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Empty cookie");
        }

        try {
            Map<String, Object> claims = jwtTokenProvider.getClaims(token);
            return ResponseEntity.ok().body(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
