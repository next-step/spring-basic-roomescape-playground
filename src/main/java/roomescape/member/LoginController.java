package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.exception.NotFoundDataException;
import roomescape.util.JwtUtil;

import java.util.Map;

@RestController
public class LoginController {

    private final MemberDao memberDao;

    public LoginController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Member member;
        try {
            member = memberDao.findByEmailAndPassword(request.email(), request.password());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundDataException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = JwtUtil.generateToken(member);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<Map<String, String>> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        if (token == null || token.isEmpty()) {
            throw new NotFoundDataException("로그인이 필요합니다.");
        }

        Long memberId = JwtUtil.getMemberIdFromToken(token);
        Member member = memberDao.findById(memberId);

        return ResponseEntity.ok(Map.of("name", member.getName()));
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
