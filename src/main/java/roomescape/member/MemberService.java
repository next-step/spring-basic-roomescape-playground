package roomescape.member;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
    public List<MemberResponse> findAll() {
        return memberDao.findAll().stream()
                .map(member -> new MemberResponse(
                        member.getId(), member.getName(), member.getEmail()
                ))
                .toList();
    }
}
