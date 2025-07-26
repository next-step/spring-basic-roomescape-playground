package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import roomescape.util.JwtUtil;
import roomescape.member.Member;
import roomescape.member.MemberDao;


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

    public Member check(String token) {
        Claims claims = jwtUtil.parseToken(token);
        String name = String.valueOf(claims.get("name"));
        return memberDao.findByName(name);
    }
}
