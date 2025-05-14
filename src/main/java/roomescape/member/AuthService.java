package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.member.dto.LoginRequest;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public AuthService(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member findMember = memberDao
                .findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        return tokenProvider.createToken(findMember);
    }

    public Member loginCheck(String token) {
        Long memberId = tokenProvider.parse(token);

        return memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
    }
}
