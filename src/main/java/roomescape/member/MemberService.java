package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenDecoder;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao,JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest){
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(),loginRequest.getPassword());

        String accessToken = jwtTokenProvider.createToken(member);

        return accessToken;
    }

    public LoginResponse checkLogin (String token) {

        Long memberId = JwtTokenDecoder.decodeToken(token);
        Member member = memberDao.findById(memberId);

        return new LoginResponse(member.getName());
    }
    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}
