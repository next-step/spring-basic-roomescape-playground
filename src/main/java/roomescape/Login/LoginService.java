package roomescape.Login;

import org.springframework.dao.DataAccessException;
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
        try {
            Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
            return jwtController.createToken(member);
        } catch (DataAccessException e){
            throw new IllegalArgumentException("로그인 정보가 일치하지 않습니다.");
        }
    }

    public MemberResponse checkLogin(String token) {
        JwtTokenMember jwtTokenMember = jwtController.extractToken(token);
        try {
            Member member = memberDao.findByName(jwtTokenMember.name());
            return new MemberResponse(member.getId(), member.getName(), member.getEmail());
        } catch (DataAccessException exception){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }

    public Member findByName(String name) {
        return memberDao.findByName(name);
    }
}
