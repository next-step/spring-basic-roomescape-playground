package roomescape.login;

import io.jsonwebtoken.Claims;
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

    public String login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);
        return memberService.generateToken(member);
    }

    public LoginCheckResponse getUserInfoFromToken(String token) {
        Claims claims = memberService.parseClaims(token);

        String memberName = claims.get("name", String.class);
        Member member = memberRepository.findByName(memberName);
        return new LoginCheckResponse(member.getName());
    }
}
