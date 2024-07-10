package roomescape.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.model.MemberLoginRequest;
import roomescape.member.model.MemberLoginResponse;
import roomescape.member.model.Member;
import roomescape.token.JwtTokenService;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;

    public roomescape.member.MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(MemberLoginRequest memberLoginRequest){
        Member member = memberRepository.findByEmailAndPassword(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());
        return jwtTokenService.create(member);
    }

    public Member checkLogin(String token) {
        MemberLoginResponse memberLoginResponse = jwtTokenService.verifyToken(token);
        Member member = memberRepository.findByName(memberLoginResponse.getName());

        if(member==null){
            throw new RuntimeException("멤버 존재 하지 않음");
        }

        return member;

    }
}
