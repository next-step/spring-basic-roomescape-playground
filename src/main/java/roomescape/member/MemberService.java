package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    @Autowired
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(),  member.getRole());
    }

    public MemberResponse loadMember(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword())
                .orElseThrow(NoSuchMemberException::new);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public MemberResponse loadMember(Long id) {
        Member member = memberDao.findById(id)
                .orElseThrow(NoSuchMemberException::new);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
