package roomescape.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.token.JwtTokenService;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final JwtTokenService jwtTokenService;

    public roomescape.member.MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest){
        roomescape.member.Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(),loginRequest.getPassword());
        return jwtTokenService.create(member);
    }

    public LoginResponse checkLogin(String token) {
        LoginResponse loginResponse = jwtTokenService.verifyToken(token);
        Member member = memberDao.findByName(loginResponse.getName());

        if(member==null){
            throw new RuntimeException("멤버 존재 하지 않음");
        }

        return LoginResponse.builder()
                .name(member.getName())
                .build();

    }
}
