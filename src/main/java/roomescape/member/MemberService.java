package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member memberLogin(MemberLoginRequest memberLoginRequest) {
        String email = memberLoginRequest.email();
        String password = memberLoginRequest.password();
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null) {
            throw new LoginFailedException("아이디 혹은 비밀번호를 확인해 주세요");
        }
        return member;
    }
}
