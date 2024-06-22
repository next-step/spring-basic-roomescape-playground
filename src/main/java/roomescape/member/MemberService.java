package roomescape.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.api.JwtDecoder;
import roomescape.api.JwtProvider;

@Service
public class MemberService {

    @Value("${roomescape.auth.jwt.secret}")
    private String secret;

    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse.Create createMember(MemberRequest.Create memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse.Create(member.getId(), member.getName(), member.getEmail());
    }

    public String loginMember(MemberRequest.Login request) {
        Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if(member == null){
            return null;
        }
        return JwtProvider.createToken(member, secret);
    }

    public MemberResponse.Check checkMember(String token) {
        Long memberId = JwtDecoder.decodeJwtToken(token);
        Member member =  memberDao.findById(memberId);
        if(member == null) {
            return null;
        }
        return MemberResponse.Check.from(member);
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}
