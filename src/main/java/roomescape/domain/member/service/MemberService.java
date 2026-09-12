package roomescape.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.entity.LoginMember;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberDao;
import roomescape.global.excpetion.NotFoundException;

import java.util.Map;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public Member createMember(String name, String email, String password) {
        return memberDao.save(new Member(name, email, password, "USER"));
    }

    public LoginMember readAuthorizedMemberById(Long memberId) {
        return memberDao.findLoginMemberById(memberId).orElseThrow(() -> new NotFoundException(memberId, Map.of("memberId", memberId), "해당 사용자를 찾을 수 없습니다."));
    }
}
