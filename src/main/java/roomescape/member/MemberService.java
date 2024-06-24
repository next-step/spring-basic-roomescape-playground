package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.token.TokenController;

@Service
public class MemberService {
    private MemberDao memberDao;
    private TokenController tokenController;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
        this.tokenController = new TokenController();
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    //email 이랑 password받아서 토큰 생성 후 리턴
    public String login(@RequestBody LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return tokenController.createToken(member);
    }

    public MemberCheckResponse memberChecking(String token) throws IllegalAccessException {
        TokenResponse tokenResponse = tokenController.verifyToken(token);
        Member member = memberDao.findByName(tokenResponse.getName());
        if (member == null) {
            throw new IllegalAccessException("Invalid");
        }
        return new MemberCheckResponse(member.getName());
    }

    public Member findByName(String name) {
        return memberDao.findByName(name);
    }
}
