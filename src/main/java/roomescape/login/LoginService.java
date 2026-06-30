package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberResponse;

@Service
public class LoginService {
    private final MemberDao memberDao;

    public LoginService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null) {
            throw new RuntimeException("Invalid email or password");
        }
        return member;
    }

    public MemberResponse checkLogin(Long memberId) {

        Member member = memberDao.findById(memberId);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }



}
