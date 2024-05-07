package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findMember(String email, String password){
        Member member=memberDao.findByEmailAndPassword(email,password);
        return new MemberResponse(member.getId(),member.getName(),member.getEmail());
    }

    public String createToken(MemberResponse memberResponse){
        String accessToken = jwtTokenProvider.createToken(memberResponse);
        return accessToken;
    }
}
