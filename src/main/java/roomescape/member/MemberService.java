package roomescape.member;

import org.springframework.stereotype.Service;

import roomescape.jwt.JwtProvider;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtProvider jwtProvider;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
        this.jwtProvider = new JwtProvider();
    }

    public String login(MemberLoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
        if(member == null){
            throw new IllegalArgumentException("Invalid email or password");
        }
        return jwtProvider.createToken(member);
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
