package roomescape.member;


import org.springframework.stereotype.Service;
import roomescape.auth.AuthService;

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
            throw new IllegalArgumentException("Invalid credentials");
        }

        LoginResponse token = authService.createToken(member.getEmail());
        return token;
    }

    public Member findByEmail(String email){
        return memberDao.findByEmail(email);
    }

    public Member findById(long id){
        return memberDao.findById(id);
    }

}
