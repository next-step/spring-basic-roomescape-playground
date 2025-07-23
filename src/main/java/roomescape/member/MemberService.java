package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private static final String ROLE = "USER";

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), ROLE));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
