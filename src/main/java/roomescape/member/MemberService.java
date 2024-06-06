package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtService;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtService jwtService;

    public MemberService(MemberDao memberDao, JwtService jwtService) {
        this.memberDao = memberDao;
        this.jwtService = jwtService;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        String jwtToken = jwtService.createJwtToken(member);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}