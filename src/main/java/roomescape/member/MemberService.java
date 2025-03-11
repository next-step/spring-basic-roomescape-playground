package roomescape.member;


import org.springframework.stereotype.Service;
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
        return memberDao.findByEmailAndPassword(email,password);
    }

    public Member findById(long id){
        return memberDao.findById(id);
    }
}
