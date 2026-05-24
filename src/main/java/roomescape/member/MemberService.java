package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginRequest;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {

    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.name(), memberRequest.email(),
                memberRequest.password(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member getMemberWithLoginRequest(LoginRequest loginRequest) {
        return memberDao.findByEmailAndPassword(
                loginRequest.email(),
                loginRequest.password());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public Member findByName(String name) {
        return memberDao.findByName(name);
    }
}
