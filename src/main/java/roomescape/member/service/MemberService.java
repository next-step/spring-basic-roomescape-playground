package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
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
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }


    public Member findByName(String name) {
        return memberDao.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 사용자가 없습니다."));
    }
}
