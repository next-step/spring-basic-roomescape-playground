package roomescape.member.service;


import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.service.AuthService;
import roomescape.exception.LoginFailedException;
import roomescape.member.dao.MemberDao;
import roomescape.member.dto.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final AuthService authService;

    public MemberService(MemberDao memberDao, AuthService authService) {
        this.memberDao = memberDao;
        this.authService = authService;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public LoginResponse login(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);

        if (member == null) {
            throw new LoginFailedException("Invalid credentials");
        }

        return authService.createToken(member.getEmail());
    }

    public Member findByEmail(String email){
        return memberDao.findByEmail(email);
    }

    public Member findById(long id){
        return memberDao.findById(id);
    }

}
