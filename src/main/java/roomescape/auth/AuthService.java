package roomescape.auth;

import jakarta.servlet.http.Cookie;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        return jwtTokenProvider.createToken(member);
    }

    public Member extractMember(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        if (token == null || token.isBlank()) {
            return null;
        }

        try {
            Long id = jwtTokenProvider.getId(token);
            String name = jwtTokenProvider.getName(token);
            String role = jwtTokenProvider.getRole(token);
            return new Member(id, name, null, role);
        } catch (RuntimeException e) {
            return null;
        }
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
}
