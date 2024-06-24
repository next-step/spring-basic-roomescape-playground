package roomescape.Login;

import org.springframework.stereotype.Service;
import roomescape.jwt.JwtController;
import roomescape.jwt.JwtTokenMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@Service
public class LoginService {
    private final MemberDao memberDao;
    private final JwtController jwtController;

    public LoginService(MemberDao memberDao, JwtController jwtController) {
        this.memberDao = memberDao;
        this.jwtController = jwtController;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return jwtController.createToken(member);
    }

    public MemberResponse checkLogin(String token) {
        JwtTokenMember jwtTokenMember = jwtController.extractToken(token);
        Member member = memberDao.findByName(jwtTokenMember.name());
        System.out.println(member.getId()+member.getName());
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findByName(String name) {
        return memberDao.findByName(name);
    }
}
