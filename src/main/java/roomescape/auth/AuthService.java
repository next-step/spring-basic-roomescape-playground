package roomescape.auth;

import java.util.Map;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        return jwtTokenProvider.createToken(member);
    }

    public LoginMember createAuthentication(String token) {
        Map<String, Object> claims = jwtTokenProvider.getClaims(token);
        return LoginMember.fromClaims(claims);
    }
}
