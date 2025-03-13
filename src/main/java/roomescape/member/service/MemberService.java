package roomescape.member.service;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.exception.LoginFailedException;
import roomescape.member.domain.Member;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.dao.MemberDao;
import roomescape.member.dto.request.MemberRequest;

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

    public Member login(String email, String password){
        try {
            return memberDao.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new LoginFailedException("Invalid email or password.");
        }
    }

    public Member findById(long id){
        return memberDao.findById(id);
    }
}
