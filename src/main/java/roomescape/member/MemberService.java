package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.util.JwtUtil;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String login(LoginRequest request) {
        Member member = memberDao.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!member.checkPassword(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return JwtUtil.createToken(member);
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
