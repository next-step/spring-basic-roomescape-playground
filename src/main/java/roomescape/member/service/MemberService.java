package roomescape.member.service;

import org.springframework.stereotype.Service;

import roomescape.member.repository.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.request.MemberRequest;
import roomescape.member.dto.response.MemberResponse;

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

    public Member findMemberByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password);
    }

    public Member findMemberByName(String name) {
        return memberDao.findByName(name);
    }
}
