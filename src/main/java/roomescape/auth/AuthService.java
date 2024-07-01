package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.api.JwtDecoder;
import roomescape.member.dto.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

@Service
public class AuthService {
    private MemberService memberService;

    public AuthService(MemberService memberService) {
        this.memberService = memberService;
    }

    public LoginMember getLoginMemberWithToken(String token) {
        Long id = JwtDecoder.decodeJwtToken(token);
        Member member = memberService.findById(id);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
