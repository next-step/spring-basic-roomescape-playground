package roomescape.login;

import org.springframework.stereotype.Service;
import roomescape.login.jwt.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class LoginService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member foundMember = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        String accessToken = jwtTokenProvider.createToken(foundMember);
        return new LoginResponse(accessToken);
    }
}
