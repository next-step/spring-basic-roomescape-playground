package roomescape;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;
import jakarta.servlet.http.Cookie;
import roomescape.JWTUtil;

@Service
public class AuthService {
    private final MemberDao memberDao;
    private final JWTUtil jwtUtil;

    public AuthService(MemberDao memberDao, JWTUtil jwtUtil) {
        this.memberDao = memberDao;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> login(MemberRequest request, HttpServletResponse response) {
        try {
            Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());
            String token = jwtUtil.createToken(member);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.ok(new MemberResponse(member.getId(), member.getName(), member.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return ResponseEntity.status(401).build();

        String token = extractTokenFromCookie(cookies);
        if (token.isEmpty()) return ResponseEntity.status(401).build();

        try {
            Claims claims = jwtUtil.parseToken(token);
            String name = claims.get("name", String.class);
            return ResponseEntity.ok(new MemberResponse(null, name, null));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
