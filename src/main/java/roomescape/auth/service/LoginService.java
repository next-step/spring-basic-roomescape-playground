package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberInfo;
import roomescape.auth.util.JwtPayload;
import roomescape.auth.util.JwtUtil;


@Service
public class LoginService {

    private final MemberDao memberDao;
    private final JwtUtil jwtUtil;

    public LoginService(MemberDao memberDao, JwtUtil jwtUtil) {
        this.memberDao = memberDao;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        return jwtUtil.createToken(member);
    }

    public MemberInfo check(String token) {
        JwtPayload payload = jwtUtil.parseToken(token);
        Member member = memberDao.findByName(payload.name());

        return new MemberInfo(
            member.getName(),
            member.getRole()
        );
    }
}
