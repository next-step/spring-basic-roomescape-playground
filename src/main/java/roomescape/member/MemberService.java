package roomescape.member;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import roomescape.JwtUtil;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final JwtUtil jwtUtil;

    public MemberService(MemberDao memberDao, JwtUtil jwtUtil) {
        this.memberDao = memberDao;
        this.jwtUtil = jwtUtil;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public void login(MemberRequest memberRequest, HttpServletResponse response) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword());
        String accessToken = jwtUtil.generateToken(member.getId().toString(), member.getName(), member.getRole());

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public String getNameFromToken(Cookie[] cookies) {
        String token = extractTokenFromCookies(cookies);
        if (token != null) {
            Claims claims = jwtUtil.parseToken(token);
            return claims.get("name", String.class);
        }
        return null;
    }

    public String getRoleFromToken(Cookie[] cookies) {
        String token = extractTokenFromCookies(cookies);
        if (token != null) {
            Claims claims = jwtUtil.parseToken(token);
            return claims.get("role", String.class);
        }
        return null;
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
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
}
