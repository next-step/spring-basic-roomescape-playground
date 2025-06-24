package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.exception.MemberNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

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
        if (member == null) {
            throw new MemberNotFoundException();
        }
        return jwtTokenProvider.generateToken(member);
    }

    public MemberResponse checkLogin(String token) {
        Long memberId = jwtTokenProvider.getMemberIdByToken(token);
        Member member = memberDao.findById(memberId);
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }

}
