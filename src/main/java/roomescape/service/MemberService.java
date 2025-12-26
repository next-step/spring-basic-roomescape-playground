package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.exception.UnauthorizedException;
import roomescape.model.Member;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member authenticate(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password).orElseThrow(() -> new UnauthorizedException("유효한 인증 정보가 없습니다."));
    }
}
