package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginRequest;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public Member authenticate(LoginRequest loginRequest) {
        try {
            return memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다");
        }
    }

    public MemberResponse findById(Long memberId) {
        Member findMember = memberDao.findById(memberId);
        return new MemberResponse(findMember.getId(), findMember.getName(), findMember.getEmail(), findMember.getRole());
    }

}
