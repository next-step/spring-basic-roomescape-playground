package roomescape.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import auth.JwtUtils;
import org.springframework.stereotype.Service;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberRequest;

@Service
public class AuthService {

    private final MemberRepository memberRepo;
    private final JwtUtils jwtUtils;

    public AuthService(MemberRepository memberRepo, JwtUtils jwtUtils) {
        this.memberRepo = memberRepo;
        this.jwtUtils = jwtUtils;
    }

    public Member login(MemberRequest request, HttpServletResponse response) {
        Member member = memberRepo
                .findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다."));
        String token = jwtUtils.createToken(member);
        Cookie cookie = createLoginCookie(token);
        response.addCookie(cookie);
        return member;
    }

    public Member checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Claims claims = jwtUtils.parseToken(token);
        String email = claims.get("email", String.class);

        return memberRepo
                .findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 회원입니다."));
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = createLogoutCookie();
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
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
