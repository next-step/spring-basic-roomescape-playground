package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtProvider;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findByToken(String token) {
        String email = jwtProvider.getEmailFromToken(token);
        if (email == null) {
            return null;
        }
        return memberDao.findByEmail(email).orElse(null);
    }
}
