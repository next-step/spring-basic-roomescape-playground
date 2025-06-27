package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;
import jakarta.servlet.http.Cookie;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JWTUtil jwtUtil;

    public AuthService(MemberDao memberDao, JWTUtil jwtUtil) {
        this.memberDao = memberDao;
        this.jwtUtil = jwtUtil;
    }

    public Member login(MemberRequest request, HttpServletResponse response) {
        Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());
        String token = jwtUtil.createToken(member);
        Cookie cookie = createLoginCookie(token);
        response.addCookie(cookie);
        return member;
    }

    public Member checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());
        if (token == null || token.isEmpty()) return null;

        try {
            Claims claims = jwtUtil.parseToken(token);
            Member member = memberDao.findByEmailAndPassword(claims.get("name", String.class), "password"); // 임시 대체
            return new Member(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (Exception e) {
            return null;
        }
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = createLogoutCookie();
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private Cookie createLoginCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private Cookie createLogoutCookie() {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
