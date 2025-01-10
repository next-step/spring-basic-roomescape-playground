package roomescape.login;

import org.springframework.stereotype.Service;
import roomescape.jwt.JwtProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@Service
public class LoginServiceImpl implements LoginService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public LoginServiceImpl(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        return jwtProvider.generateToken(member);
    }

    @Override
    public MemberResponse validateToken(String token) {

        if (!jwtProvider.isValidToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        Long memberId = Long.valueOf(jwtProvider.extractSubject(token));
        Member member = memberDao.findByName(memberId.toString());

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
