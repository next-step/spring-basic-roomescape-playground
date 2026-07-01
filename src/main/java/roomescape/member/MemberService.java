package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(
                        memberRequest.getName(),
                        memberRequest.getEmail(),
                        memberRequest.getPassword(),
                        "USER"
                )
        );

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member login(LoginRequest loginRequest) {
        return memberDao.findByEmailAndPassword(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
    }

    public LoginCheckResponse checkLogin(LoginMember loginMember) {
        return new LoginCheckResponse(loginMember.getName());
    }

    public LoginMember findLoginMemberById(Long memberId) {
        Member member = memberDao.findById(memberId);

        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
