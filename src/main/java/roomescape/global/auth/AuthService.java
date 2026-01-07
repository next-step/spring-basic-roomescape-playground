package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.AuthenticationException;
import roomescape.global.token.JwtTokenProvider;
import roomescape.member.entity.Member;
import roomescape.member.dao.MemberDao;

@Service
@Transactional
public class AuthService {
    private JwtTokenProvider jwtTokenProvider;
    private MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public Member extractMember(HttpServletRequest request) {
        String token = extractTokenFromCookie(request);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("인증되지 않은 사용자입니다.");
        }

        String email = jwtTokenProvider.getPayload(token);
        return memberDao.findByEmail(email);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
