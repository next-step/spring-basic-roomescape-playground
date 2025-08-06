package roomescape.member;

import static roomescape.member.Role.USER;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse create(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    @Transactional(readOnly = true)
    public Member findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("토큰의 사용자 정보가 유효하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member findByName(String name) {
        return memberDao.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
