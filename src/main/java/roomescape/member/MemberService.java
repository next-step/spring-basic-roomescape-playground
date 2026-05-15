package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginCheckResponse;
import roomescape.auth.LoginRequest;

@Service
public class MemberService {

    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(),
                memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member getMemberWithLoginRequest(LoginRequest loginRequest) {
        return memberDao.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword());
    }

    public LoginCheckResponse getLoginCheckInfo(Long memberId) {
        Member member = memberDao.findById(memberId);
        return new LoginCheckResponse(member.getName());
    }
}
