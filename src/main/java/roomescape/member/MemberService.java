package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.exception.MemberException;

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

    public Long login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new MemberException(MemberErrorCode.LOGIN_FAILED));
        return member.getId();
    }

    public Member getMember(Long memberId) {
        return memberDao.findById(memberId);
    }
}
