package roomescape.member;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.MemberAuthContext;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberAuthContext loginByEmailAndPassword(MemberLoginRequest request) {
        try {
            Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
            return new MemberAuthContext(member.getName(), member.getEmail());
        } catch (DataAccessException exception) {
            throw new IllegalArgumentException("로그인 정보가 불일치 합니다.");
        }
    }

    public Member checkLogin(MemberAuthContext authContext) {

    }
}
