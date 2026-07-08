package roomescape.login;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberResponse;

@Service
public class LoginService {
    private final MemberRepository memberDao;

    public LoginService(MemberRepository memberDao) {
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
        if (member == null) {
            throw new RuntimeException("Invalid email or password");
        }
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(Long id) {
        Member member = memberDao.findById(id);

        return member;
    }


}
