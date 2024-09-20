package roomescape.member;

import org.springframework.stereotype.Service;

import roomescape.auth.JwtProvider;

@Service
public class MemberService {

    private MemberDao memberDao;
    private JwtProvider jwtProvider;

    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
            new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String memberLogin(MemberLoginRequest request) {
        Member member = memberDao.getByEmailAndPassword(request.email(), request.password());
        return jwtProvider.createToken(member);
    }

    public MemberLoginCheckResponse getMemberNameByToken(String token) {
        String name = jwtProvider.getMemberName(token);
        return new MemberLoginCheckResponse(name);
    }
}
