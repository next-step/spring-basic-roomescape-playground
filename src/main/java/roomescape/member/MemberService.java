package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        return jwtTokenProvider.createToken(member);
    }
}
