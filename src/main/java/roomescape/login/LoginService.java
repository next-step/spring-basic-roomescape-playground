package roomescape.login;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberService;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public LoginService(MemberRepository memberRepository, MemberService memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return memberService.generateToken(member);
    }

    public String getUserInfoFromToken(String token) {
        String memberName = memberService.getClaimValue(token, "name");
        Member member = memberRepository.findByName(memberName);
        return member.getName();
    }
}
