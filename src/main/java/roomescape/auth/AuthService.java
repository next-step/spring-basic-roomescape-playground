package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.jwt.TokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public AuthService(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public String createToken(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(),
            loginRequest.getPassword());
        return tokenProvider.createToken(member);
    }

    public LoginMember login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        return new LoginMember(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getRole()
        );
    }
}
