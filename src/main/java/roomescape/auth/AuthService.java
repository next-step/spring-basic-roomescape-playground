package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenUtil;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.token.TokenRequest;
import roomescape.token.TokenResponse;

@Service
public class AuthService {
    private JwtTokenUtil jwtTokenUtil;
    private MemberDao memberDao;

    public AuthService(JwtTokenUtil jwtTokenUtil, MemberDao memberDao) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.memberDao = memberDao;
    }

    public Member checkInvalidLogin(String principal, String credentials) {
        Member member = null;
        try {
        member = memberDao.findByEmailAndPassword(principal, credentials);
        } catch(AuthorizationException e) {
            e.printStackTrace();
        }
        return member;
    }

    public Long findMemberIdByToken(HttpServletRequest request) {
        return jwtTokenUtil.getPayload(request);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword());
        String accessToken = jwtTokenUtil.createToken(member);
        return new TokenResponse(accessToken);
    }
}
