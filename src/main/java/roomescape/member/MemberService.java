package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

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

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public Member findByName(String name) {
        return memberDao.findByName(name);
    }

    public LoginCheckResponse getLoginCheckInfo(Long memberId) {
        Member member = memberDao.findById(memberId);
        return new LoginCheckResponse(member.getName());
    }
}
