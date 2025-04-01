package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.enums.Role;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = registerMember(memberRequest);
        return toMemberResponse(member);
    }

    private Member registerMember(MemberRequest memberRequest) {
        return memberDao.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), Role.USER));
    }

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

}
