package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {
    private final MemberDao memberDao;

    public AuthService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member authenticateMember(LoginRequest loginRequest) {
        return memberDao.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ).orElseThrow(
                () -> new RuntimeException("존재하지 않는 회원입니다.")
        );
    }
}
