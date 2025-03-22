package roomescape.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.util.JwtUtil;

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
        return jwtUtil.generateToken(member);
    }

    public LoginCheckResponse getUserInfoFromToken(String token) {
        Claims claims = jwtUtil.parseClaims(token);

        String memberName = claims.get("name", String.class);
        Member member = memberDao.findByName(memberName);
        return new LoginCheckResponse(member.getName());
    }
}
