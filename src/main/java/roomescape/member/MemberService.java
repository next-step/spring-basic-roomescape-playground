package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.exception.MemberNotFoundException;

@Service
public class MemberService {
    
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member getMemberById(Long id) {
        Member member = memberDao.findById(id);
        if(member == null) {
            throw new MemberNotFoundException();
        }
        return member;
    }

}
