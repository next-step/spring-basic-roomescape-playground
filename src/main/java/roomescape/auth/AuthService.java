package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider  jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return jwtTokenProvider.generateToken(member);
    }

}
