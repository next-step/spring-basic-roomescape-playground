package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.InvalidTokenException;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member authenticate(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return member;
    }

    public MemberResponse findByEmail(String email) {
        Member member = memberDao.findByEmail(email);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public LoginMember findLoginMemberByEmail(String email) {
        try {
            Member member = memberDao.findByEmail(email);
            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidTokenException("토큰에 해당하는 회원이 없습니다.", e);
        }
    }

}
