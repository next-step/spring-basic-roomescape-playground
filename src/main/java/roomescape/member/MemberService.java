package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.member.dto.LoginRequest;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public MemberService(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest) {
        Member findMember = memberDao
                .findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        return tokenProvider.createToken(findMember);
    }

    public Member loginCheck(String token) {
        Long memberId = tokenProvider.parse(token);


        return memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
    }
}
