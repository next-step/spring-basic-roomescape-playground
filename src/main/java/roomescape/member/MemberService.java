package roomescape.member;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return MemberResponse.from(member);
    }

    public Member login(LoginRequest loginRequest, HttpServletResponse response) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (member == null) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    public Member findByToken(Long memberId) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return member;
    }
}
