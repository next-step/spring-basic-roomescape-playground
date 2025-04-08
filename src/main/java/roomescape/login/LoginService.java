package roomescape.login;

import org.springframework.stereotype.Service;
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

    public String loginAndGetToken(LoginRequest loginRequest) {
        // 1. 멤버 조회
        final Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(),
                                                                           loginRequest.getPassword());
        // 2. 토큰 생성
        return jwtTokenProvider.getAccessToken(member);
    }

}
